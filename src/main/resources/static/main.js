const urlPrefix = '/api';
let currentUser = null;

// Функция для получения текущего пользователя с сервера
const getCurrentUser = async () => {
  try {
    const response = await fetch(urlPrefix + '/current-user', {
      method: 'GET',
      credentials: 'include' // Важно для передачи куки/сессии
    });
    if (!response.ok) throw new Error('Failed to fetch current user');
    return response.json();
  } catch (error) {
    console.error('Error fetching current user:', error);
    return null;
  }
};

let allUsers = [];
let allRoles = [];

/** Выполняет HTTP-запрос к API */
const request = async (url, method, body) => {
  try {
    const response = await fetch(urlPrefix + url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: body ? JSON.stringify(body) : null
    });
    if (!response.ok) throw new Error('Failed to fetch');
    return response.json();
  } catch (error) {
    console.error('Error:', error);
  }
};

/** Логин: отправка данных на сервер и обновление интерфейса */
const login = async (username, password) => {
  try {
    const response = await fetch(urlPrefix + '/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
      credentials: 'include' // Для сохранения сессии
    });

    if (!response.ok) throw new Error('Login failed');

    // После успешного логина получаем текущего пользователя
    currentUser = await getCurrentUser();
    updateDisplay();
  } catch (error) {
    console.error('Login error:', error);
    alert('Login failed: ' + error.message);
  }
};

/** Выход: очистка данных и обновление интерфейса */
const logout = async () => {
  try {
    await fetch(urlPrefix + '/logout', {
      method: 'POST',
      credentials: 'include'
    });
    localStorage.removeItem("currentUser");
    currentUser = null;
    removeEventListeners();
    window.location.reload();
  } catch (error) {
    console.error('Logout error:', error);
  }
};

/** Проверка авторизации */
const checkAuth = async () => {
  try {
    const user = await getCurrentUser();
    if (user) {
      currentUser = user;
      return true;
    }
    return false;
  } catch (error) {
    return false;
  }
};

/** Обновление отображения в зависимости от состояния авторизации */
function updateDisplay() {
  const loginFormContainer = document.getElementById('loginFormContainer');
  const mainContent = document.getElementById('mainContent');
  const navbar = document.getElementById('navbar');
  const sidebar = document.getElementById('sidebar');
  const loginForm = document.getElementById('loginForm');

  const loginHandler = async (e) => {
    e.preventDefault();
    const formData = new FormData(loginForm);
    await login(formData.get("username"), formData.get("password"));
  };

  if (currentUser) {
    loginFormContainer.style.display = 'none';
    mainContent.style.display = 'block';
    navbar.style.display = 'block';
    sidebar.style.display = 'block';

    const isAdmin = currentUser.roles.some(role => role.name === "ROLE_ADMIN");
    document.getElementById('adminLink').style.display = isAdmin ? 'block' : 'none';
    document.getElementById('userLink').style.display = 'block';

    loginForm.removeEventListener('submit', loginHandler);
    initContent(); // <-- Запускаем инициализацию контента
  } else {
    loginFormContainer.style.display = 'flex';
    mainContent.style.display = 'none';
    navbar.style.display = 'none';
    sidebar.style.display = 'none';
    loginForm.addEventListener('submit', loginHandler);
  }
}

/** Получение пользователей с сервера */
const getUsers = async () => {
  const users = await request("/users", "GET");
  allUsers = users || [];
  populateUsersTable();
};

/** Получение ролей с сервера */
const getRoles = async () => {
  const roles = await request("/roles", "GET");
  allRoles = roles || [];
  populateRolesSelect();
  populateEditRolesSelect();
};

/** Инициализация данных и отображения */
const init = async () => {
  const isAuthenticated = await checkAuth();
  if (isAuthenticated) {
    await getRoles();
    await getUsers();
    initContent();
    document.getElementById('adminLink').click();
  }
  updateDisplay();
};

/** Инициализация контента для авторизованных пользователей */
function updateUserInfoTable() {
  if (!currentUser) return;

  const tbody = document.querySelector('#userPanel #userInfoTable');
  if (!tbody) return; // Если таблицы нет, выходим

  const rolesText = currentUser.roles.map(r => r.name).join(', ');
  tbody.innerHTML = `
    <tr>
      <td>${currentUser.id}</td>
      <td>${currentUser.username}</td>
      <td>${currentUser.email}</td>
      <td>${rolesText}</td>
    </tr>
  `;
}
function initContent() {
  updateUserInfoDisplay();
  populateRolesSelect();
  populateEditRolesSelect();
  populateUsersTable();

  const isAdmin = currentUser?.roles.some(role => role.name === "ROLE_ADMIN");

  // Изначально скрываем обе панели
  document.getElementById('adminPanel').style.display = 'none';
  document.getElementById('userPanel').style.display = 'none';

  // Обновляем таблицу User Information
  updateUserInfoTable();

  // В зависимости от роли показываем нужную панель
  if (isAdmin) {
    document.getElementById('adminPanel').style.display = 'block';
    // Активируем вкладку Admin
    document.getElementById('adminLink').classList.add('active');
    document.getElementById('userLink').classList.remove('active');
  } else {
    document.getElementById('userPanel').style.display = 'block';
    // Активируем вкладку User
    document.getElementById('userLink').classList.add('active');
    document.getElementById('adminLink').classList.remove('active');
  }

  setupEventListeners();
}

/** Инициализация модальных окон Bootstrap */
const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
const deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));

/** Добавление нового пользователя */
const postNewUser = async function(e) {
  const formData = new FormData(this);
  const r = allRoles.filter(r => formData.getAll('roleIds').includes(r.id.toString()))
  const roles = r.map((role) => {
    return {
      "role_id": role.id,
      name: role.name,
    };
  })
  console.log(r, roles);
  const userData = {
    username: formData.get('username'),
    email: formData.get('email'),
    password: formData.get('password'),
    roles,
  };
  const newUser = await request("/users", "POST", userData);
  allUsers.push(newUser);
  this.reset();
  initContent();
  populateUsersTable();
};

/** Удаление пользователя */
const deleteUser = async (userId) => {
  await request(`/users/${userId}`, "DELETE");
  allUsers = allUsers.filter(u => u.id !== userId);
  populateUsersTable();
};

/** Редактирование пользователя */
const editUser = async function(e) {
  try {
    e.preventDefault();
    const formData = new FormData(this);
    const userId = parseInt(formData.get('id'));
    const userData = {
      id: userId,
      username: formData.get("username"),
      email: formData.get("email"),
      password: formData.get("password"),
      roles: allRoles.filter(r => formData.getAll('roleIds').includes(r.id.toString()))
    };
    const editedUser = await request(`/users/${userId}`, "PUT", userData);
    allUsers = allUsers.map(user => user.id === userId ? editedUser : user);
    populateUsersTable();
    this.reset();
    editModal.hide();
  } catch (error){
    alert(error.message);
  }
};

/** Заполнение таблицы пользователей */
const populateUsersTable = () => {
  const tbody = document.querySelector('#usersTable tbody');
  tbody.innerHTML = '';
  allUsers.forEach(user => {
    const tr = document.createElement('tr');
    if (user.id === currentUser.id) {
      tr.classList.add('active-user');
    }
    const rolesText = user.roles.map(r => r.simpleName).join(', ');
    tr.innerHTML = `
      <td>${user.id}</td>
      <td>${user.username}</td>
      <td>${user.email}</td>
      <td>${rolesText}</td>
      <td>
        <button class="btn btn-warning btn-sm edit-btn" data-user-id="${user.id}">Edit</button>
        <button class="btn btn-danger btn-sm delete-btn" data-user-id="${user.id}">Delete</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
};

/** Заполнение выпадающего списка ролей для формы создания пользователя */
const populateRolesSelect = () => {
  const select = document.getElementById('rolesSelect');
  select.innerHTML = '';
  allRoles.forEach(role => {
    const option = document.createElement('option');
    option.value = role.id;
    option.textContent = role.name;
    select.appendChild(option);
  });
};

/** Заполнение выпадающего списка ролей для формы редактирования */
const populateEditRolesSelect = () => {
  const select = document.getElementById('editRoles');
  if (!select) return;
  select.innerHTML = '';
  const fragment = document.createDocumentFragment();
  allRoles.forEach(role => {
    const option = document.createElement('option');
    option.value = role.id;
    option.textContent = role.name;
    fragment.appendChild(option);
  });
  select.appendChild(fragment);
};

/** Обновление информации о текущем пользователе */
const updateUserInfoDisplay = () => {
  if (!currentUser) return;
  const rolesText = currentUser.roles.map(r => r.name).join(', ');
  document.getElementById('currentUserInfo').textContent = `${currentUser.email} with roles: ${rolesText}`;
};

/** Функции переключения между панелями */
const adminLinkFn = (e) => {
  e.preventDefault();
  document.getElementById('adminPanel').style.display = 'block';
  document.getElementById('userPanel').style.display = 'none';
  e.target.classList.add('active');
  document.getElementById('userLink').classList.remove('active');
};

const userLinkFn = (e) => {
  e.preventDefault();
  document.getElementById('adminPanel').style.display = 'none';
  document.getElementById('userPanel').style.display = 'block';
  e.target.classList.add('active');
  document.getElementById('adminLink').classList.remove('active');
};

/** Обработчик кликов по кнопкам Edit/Delete в таблице пользователей */
const userTableHandler = (e) => {
  if (e.target.classList.contains('edit-btn')) {
    const userId = parseInt(e.target.getAttribute('data-user-id'));
    openEditModal(userId);
  } else if (e.target.classList.contains('delete-btn')) {
    const userId = parseInt(e.target.getAttribute('data-user-id'));
    openDeleteModal(userId);
  }
};

/** Подтверждение удаления пользователя */
const confirmDeleteFn = () => {
  const userId = parseInt(document.getElementById('deleteUserId').value);
  deleteUser(userId);
  deleteModal.hide();
};

/** Обработчик выхода */
const logoutFn = (e) => {
  e.preventDefault();
  logout();
};

/** Настройка всех слушателей событий */
const setupEventListeners = () => {
  const isAdmin = currentUser && currentUser.roles.some(role => role.name === "ROLE_ADMIN");

  if (isAdmin) {
    document.getElementById('adminLink').addEventListener('click', adminLinkFn);
    document.getElementById('newUserForm').addEventListener('submit', postNewUser);
    document.querySelector('#usersTable tbody').addEventListener('click', userTableHandler);
    document.getElementById('editUserForm').addEventListener('submit', editUser);
    document.getElementById('confirmDeleteBtn').addEventListener('click', confirmDeleteFn);
  }

  // Эти слушатели для всех пользователей
  document.getElementById('userLink').addEventListener('click', userLinkFn);
  document.getElementById('logoutForm').addEventListener('submit', logoutFn);
};

/** Удаление слушателей событий */
const removeEventListeners = () => {
  document.getElementById('adminLink').removeEventListener('click', adminLinkFn);
  document.getElementById('userLink').removeEventListener('click', userLinkFn);
  document.getElementById('newUserForm').removeEventListener('submit', postNewUser);
  document.querySelector('#usersTable tbody').removeEventListener('click', userTableHandler);
  document.getElementById('editUserForm').removeEventListener('submit', editUser);
  document.getElementById('confirmDeleteBtn').removeEventListener('click', confirmDeleteFn);
  document.getElementById('logoutForm').removeEventListener('submit', logoutFn);
};

/** Открытие модального окна редактирования и заполнение формы */
const openEditModal = (userId) => {
  const user = allUsers.find(u => u.id === userId);
  if (!user) return;
  document.getElementById('editUserId').value = user.id;
  document.getElementById('editUsername').value = user.username;
  document.getElementById('editEmail').value = user.email;
  document.getElementById('editPassword').value = '';
  
  const editRolesSelect = document.getElementById('editRoles');
  Array.from(editRolesSelect.options).forEach(option => {
    option.selected = user.roles.some(r => r.id === parseInt(option.value));
  });
  editModal.show();
};

/** Открытие модального окна удаления */
const openDeleteModal = (userId) => {
  const user = allUsers.find(u => u.id === userId);
  if (!user) return;
  document.getElementById('deleteUserId').value = user.id;
  document.getElementById('deleteUsername').textContent = user.username;
  deleteModal.show();
};

/** Инициализация после загрузки страницы */
document.addEventListener('DOMContentLoaded', async () => {
  const isAuthenticated = await checkAuth();
  if (isAuthenticated) {
    await getRoles();
    await getUsers();
  }
  updateDisplay();
});