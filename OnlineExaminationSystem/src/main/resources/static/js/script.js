const API_BASE = "http://localhost:8080/api";

/* ======================================================
   🔐 AUTHENTICATION (Login / Register)
====================================================== */

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
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const user = await response.json();
      localStorage.setItem("user", JSON.stringify(user));

      if (user.role === "FACULTY") {
        window.location = "faculty_dashboard.html";
      } else {
        window.location = "exam.html";
      }
    } else {
      alert("Invalid username or password.");
    }
  } catch (err) {
    console.error("Login error:", err);
    alert("Error connecting to server.");
  }
}

async function register() {
  try {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const role = document.getElementById("role").value;

    if (!username || !password) {
      alert("Please fill all fields!");
      return;
    }

    const res = await fetch(`${API_BASE}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password, role }),
    });

    if (res.ok) {
      alert("Registration successful! You can now login.");
      window.location = "login.html";
    } else {
      alert("Registration failed.");
    }
  } catch (err) {
    console.error("Register error:", err);
  }
}

/* ======================================================
   🧾 FACULTY: CREATE EXAM
====================================================== */

async function createExam() {
  const title = document.getElementById("examTitle").value.trim();
  const duration = document.getElementById("examDuration").value.trim();
  const faculty = JSON.parse(localStorage.getItem("user")).username;

  if (!title || !duration) {
    alert("Please fill all fields!");
    return;
  }

  const res = await fetch(`${API_BASE}/exams/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      title,
      durationMinutes: parseInt(duration),
      createdBy: faculty,
    }),
  });

  if (res.ok) {
    const exam = await res.json();
    localStorage.setItem("currentExamId", exam.id);
    alert(`✅ Exam "${exam.title}" created! (Exam ID: ${exam.id})`);
  } else {
    alert("Failed to create exam.");
  }
}

/* ======================================================
   ✏️ FACULTY: ADD QUESTIONS
====================================================== */

async function addQuestion() {
  const examId = localStorage.getItem("currentExamId");

  if (!examId) {
    alert("Please create or select an exam first!");
    return;
  }

  const q = {
    questionText: document.getElementById("questionText").value.trim(),
    optionA: document.getElementById("optionA").value.trim(),
    optionB: document.getElementById("optionB").value.trim(),
    optionC: document.getElementById("optionC").value.trim(),
    optionD: document.getElementById("optionD").value.trim(),
    correctAnswer: document.getElementById("correctAnswer").value.trim(),
    exam: { id: parseInt(examId) },
  };

  const res = await fetch(`${API_BASE}/questions/add`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(q),
  });

  if (res.ok) {
    alert("✅ Question added successfully!");
    document.getElementById("questionForm").reset();
  } else {
    alert("❌ Failed to add question. Check backend logs.");
  }
}

/* ======================================================
   🎓 STUDENT: LOAD EXAMS LIST (with DTO)
====================================================== */

async function loadExams() {
  try {
    const res = await fetch(`${API_BASE}/exams/all`);
    if (!res.ok) throw new Error("Failed to fetch exams");

    const exams = await res.json();
    const examSelect = document.getElementById("examSelect");

    examSelect.innerHTML = "<option value=''>-- Select Exam --</option>";

    exams.forEach((exam) => {
      const option = document.createElement("option");
      option.value = exam.id;
      option.textContent = `${exam.title} (ID: ${exam.id})`;
      examSelect.appendChild(option);
    });
  } catch (err) {
    console.error("Error loading exams:", err);
    alert("Could not load exams. Check backend connection.");
  }
}

/* ======================================================
   ▶️ STUDENT: START EXAM
====================================================== */

function startExam() {
  const selectedExamId = document.getElementById("examSelect").value;

  if (!selectedExamId) {
    alert("Please select an exam!");
    return;
  }

  localStorage.setItem("currentExamId", selectedExamId);
  window.location = "exam_start.html";
}

/* ======================================================
   🧠 STUDENT: LOAD QUESTIONS BY EXAM ID
====================================================== */

async function loadQuestions() {
  const examId = localStorage.getItem("currentExamId");

  if (!examId) {
    alert("No exam selected.");
    return;
  }

  const res = await fetch(`${API_BASE}/questions/exam/${examId}`);
  const questions = await res.json();
  const container = document.getElementById("questionSection");

  if (!questions.length) {
    container.innerHTML = "<p>No questions found for this exam.</p>";
    return;
  }

  container.innerHTML = questions
    .map(
      (q, i) => `
    <div class="question" data-id="${q.id}">
      <h4>${i + 1}. ${q.questionText}</h4>
      <label><input type="radio" name="q${q.id}" value="A"> ${q.optionA}</label><br>
      <label><input type="radio" name="q${q.id}" value="B"> ${q.optionB}</label><br>
      <label><input type="radio" name="q${q.id}" value="C"> ${q.optionC}</label><br>
      <label><input type="radio" name="q${q.id}" value="D"> ${q.optionD}</label>
    </div>
    <hr>`
    )
    .join("");

  startTimer(questions.length * 60);
}

/* ======================================================
   ⏳ TIMER FUNCTION
====================================================== */

function startTimer(durationSeconds) {
  const timerElement = document.getElementById("timeLeft");
  let timeLeft = durationSeconds;

  const timer = setInterval(() => {
    const minutes = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    timerElement.textContent = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;

    if (--timeLeft < 0) {
      clearInterval(timer);
      alert("⏰ Time’s up! Submitting exam automatically...");
      submitExam();
    }
  }, 1000);
}

/* ======================================================
   📤 SUBMIT EXAM
====================================================== */

async function submitExam() {
  const examId = localStorage.getItem("currentExamId");
  const user = JSON.parse(localStorage.getItem("user"));

  if (!examId || !user) {
    alert("Missing exam or user data.");
    return;
  }

  const questionDivs = document.querySelectorAll(".question");
  const answers = [];

  questionDivs.forEach((q) => {
    const qId = q.dataset.id;
    const selected = q.querySelector("input[type='radio']:checked");
    if (selected) {
      answers.push({ questionId: parseInt(qId), selectedAnswer: selected.value });
    }
  });

  const res = await fetch(`${API_BASE}/exams/submit`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      userId: user.id,
      examId: parseInt(examId),
      answers,
    }),
  });

  if (res.ok) {
    const result = await res.json();
    alert(`✅ Exam submitted successfully! 🎯 Score: ${result.score}`);
  } else {
    alert("Exam submission failed.");
  }
}

/* ======================================================
   💬 FEEDBACK SYSTEM
====================================================== */

async function submitFeedback() {
  const user = JSON.parse(localStorage.getItem("user"));
  const feedback = {
    userId: user.id,
    examId: localStorage.getItem("currentExamId"),
    comments: document.getElementById("feedbackText").value,
    rating: document.getElementById("rating").value,
  };

  const res = await fetch(`${API_BASE}/feedback/submit`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(feedback),
  });

  if (res.ok) {
    alert("Feedback submitted successfully!");
  } else {
    alert("Failed to submit feedback.");
  }
}
