// Shared script for login, register, and todos pages
const SERVER_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// Login page logic
function login() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  fetch(`${SERVER_URL}/auth/login`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({ email, password }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(data.message || "login failed");
      }
      return response.json();
    })
    .then((data) => {
      localStorage.setItem("token", data.token);
      window.location.href = "todos.html";
    })
    .catch((error) => {
      alert(error.message);
    });
}

// Register page logic
function register() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  fetch(`${SERVER_URL}/auth/register`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({ email, password }),
  })
    .then((response) => {
      if (response.ok) {
        alert("Registration successfull! Please Login!");
        window.location.href = "login.html";
      } else {
        return response.json().then((data) => {
          throw new Error(data.message || "Registration failed");
        });
      }
    })
    .catch((error) => {
      alert(error.message);
    });
}

// Todos page logic
function createTodoCard(todo) {
  const card = document.createElement("div");
  card.className = "todo-card";
  const checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.checked = todo.isCompleted;
  checkbox.addEventListener("change", function () {
    const updateTodo = { ...todo, isCompleted: checkbox.checked };
    updateTodoStatus(updateTodo);
  });

  const span = document.createElement("span");
  span.textContent = todo.title;

  if (todo.isCompleted) {
    span.style.textDecoration = "line-through";
    span.style.color = "#aaa";
  }

  const deleteBtn = document.createElement("button");
  deleteBtn.textContent = "X";
  deleteBtn.onclick = function () {
    deleteTodo(todo.id);
  };
  //element visibe in page
  card.appendChild(checkbox);
  card.appendChild(span);
  card.appendChild(deleteBtn);

  return card;
}

function loadTodos() {
  if (!token) {
    alert("Please login first!");
    window.location.href = "login.html"; // Redirect to login page if token is not available
    return;
  }

  fetch(`${SERVER_URL}/api/v1/todo`, {
    method: "GET",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    //body: JSON.stringify(todo),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(data.message || "failed to get todo");
      }
      return response.json();
    })
    .then((todos) => {
      const todoList = document.getElementById("todo-list");
      todoList.innerHTML = "";
      //todo empty means below code will execute.
      if (!todos || todos.length == 0) {
        todoList.innerHTML = `<p id="empty-message">No todos yet. Add one below</p>`;
      } else {
        todos.forEach((todo) => {
          todoList.appendChild(createTodoCard(todo));
        });
      }
    })
    .catch((error) => {
      alert(error.message);
      document.getElementById("todo-list").innerHTML =
        `<p style="color:red" id="empty-message">No todos yet.Add one below</p>`;
    });
}

function addTodo() {
  //console.log("Current Token value:", token);
  const input = document.getElementById("new-todo");
  const todoText = input.value.trim();

  if (!todoText) {
    return;
  }

  fetch(`${SERVER_URL}/api/v1/todo/create`, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      title: todoText,
      isCompleted: false,
    }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(data.message || "failed to add todo");
      }
      return response.json();
    })
    .then((newTodo) => {
      input.value = ""; // Clear the input field after adding a new todo
      loadTodos();
    })
    .catch((error) => {
      alert(error.message);
    });
}

function updateTodoStatus(todo) {
  fetch(`${SERVER_URL}/api/v1/todo`, {
    method: "PUT",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(todo),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(data.message || "failed to update todo");
      }
      return response.json();
    })
    .then(() => {
      loadTodos();
    })
    .catch((error) => {
      alert(error.message);
    });
}

function deleteTodo(id) {
  fetch(`${SERVER_URL}/api/v1/todo/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(data.message || "failed to delete todo");
      }
      return response.text();
    })
    .then(() => {
      loadTodos();
    })
    .catch((error) => {
      alert(error.message);
    });
}

// Page-specific initializations
document.addEventListener("DOMContentLoaded", function () {
  if (document.getElementById("todo-list")) {
    loadTodos();
  }
});
