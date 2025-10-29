const API_BASE = "http://localhost:8080/api";

async function login() {
  try {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (!username || !password) {
      alert("Please enter both username and password.");
      return;
    }

    const response = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (response.ok) {
      const user = await response.json();
      localStorage.setItem("user", JSON.stringify(user));
      if (user.role === "FACULTY") window.location = "faculty_dashboard.html";
      else window.location = "exam.html";
    } else {
      const errText = await response.text();
      console.error("Login failed:", errText);
      alert("Invalid credentials or server not reachable.");
    }
  } catch (error) {
    console.error("Unexpected error in login:", error);
    alert("An unexpected error occurred during login.");
  }
}

async function register() {
  try {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const role = document.getElementById("role").value;

    if (!username || !password) {
      alert("Please fill in all fields.");
      return;
    }

    const response = await fetch(`${API_BASE}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password, role })
    });

    if (response.ok) {
      alert("Registered successfully!");
    } else {
      const errText = await response.text();
      console.error("Registration failed:", errText);
      alert("Registration failed. Check console for details.");
    }
  } catch (error) {
    console.error("Unexpected error in register:", error);
    alert("An unexpected error occurred during registration.");
  }
}


async function createExam() {
  const title = document.getElementById("examTitle").value;
  const duration = document.getElementById("examDuration").value;
  const faculty = JSON.parse(localStorage.getItem("user")).username;

  await fetch(`${API_BASE}/exams/create`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({title, durationMinutes: duration, createdBy: faculty})
  });
  alert("Exam Created!");
}

async function addQuestion() {
  const q = {
    questionText: document.getElementById("questionText").value,
    optionA: document.getElementById("optionA").value,
    optionB: document.getElementById("optionB").value,
    optionC: document.getElementById("optionC").value,
    optionD: document.getElementById("optionD").value,
    correctAnswer: document.getElementById("correctAnswer").value
  };

  await fetch(`${API_BASE}/questions/add`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(q)
  });
  alert("Question Added!");
}

async function loadQuestions() {
  const examId = localStorage.getItem("examId"); // Set this when student selects exam
  const res = await fetch(`${API_BASE}/questions/exam/${examId}`);
  const questions = await res.json();

  const container = document.getElementById("questionSection");
  console.log("Loaded questions:", questions);

  container.innerHTML = questions.map((q, i) => `
    <div data-id="${q.id}">
      <h4>${i + 1}. ${q.questionText}</h4>
      <label><input type="radio" name="q${q.id}" value="A"> ${q.optionA}</label><br>
      <label><input type="radio" name="q${q.id}" value="B"> ${q.optionB}</label><br>
      <label><input type="radio" name="q${q.id}" value="C"> ${q.optionC}</label><br>
      <label><input type="radio" name="q${q.id}" value="D"> ${q.optionD}</label>
    </div><hr>
  `).join('');
}


async function submitExam() {
  const res = await fetch(`${API_BASE}/questions/`);
  const questions = await res.json();
  let score = 0;

 questions.forEach(q => {
    const selected = document.querySelector(`input[name="q${q.id}"]:checked`);
    console.log(`Q${q.id} selected:`, selected?.value, "correct:", q.correctAnswer);
    if (selected && selected.value.trim().toUpperCase() === q.correctAnswer.trim().toUpperCase()) {
        score++;
    }
});


  const user = JSON.parse(localStorage.getItem("user"));
  const result = {
    userId: user.id,
    examId: 1,
    score,
    percentage: (score / questions.length) * 100
  };

  await fetch(`${API_BASE}/results/save`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(result)
  });

  alert(`Exam submitted! Score: ${score}`);
  window.location = "results.html";
}

async function submitFeedback() {
  const user = JSON.parse(localStorage.getItem("user"));
  const feedback = {
    userId: user.id,
    examId: 1,
    comments: document.getElementById("feedbackText").value,
    rating: document.getElementById("rating").value
  };

  await fetch(`${API_BASE}/feedback/submit`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(feedback)
  });

  alert("Feedback submitted successfully!");
}
