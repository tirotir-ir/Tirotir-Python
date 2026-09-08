package com.example.data

data class ProjectItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val iconName: String,
    val sourceCode: String
)

object ProjectData {
  val projects: List<ProjectItem> = listOf(
      ProjectItem(
          id = "calc",
          title = "Interactive Calculator",
          description = "Full arithmetic expression evaluator with standard GUI and corresponding Python source code view.",
          category = "GUI & Math",
          iconName = "Calculate",
          sourceCode = """# tirOtir AI Academy - Interactive Calculator
def calculate(expr):
    try:
        # Safe evaluation of arithmetic expressions
        allowed = "0123456789+-*/.() "
        if all(c in allowed for c in expr):
            result = eval(expr)
            return f"Result: {result}"
        return "Error: Invalid characters"
    except ZeroDivisionError:
        return "Error: Division by zero"
    except Exception as e:
        return f"Error: {e}"

# Test cases
print(calculate("15 + 25 * 2"))
print(calculate("(100 - 16) / 4"))
print(calculate("2 ** 8"))
"""
      ),
      ProjectItem(
          id = "morse",
          title = "Morse Code Converter",
          description = "Converts English text into International Morse Code with animated audio/visual transmission pulses.",
          category = "Signals & Encoding",
          iconName = "GraphicEq",
          sourceCode = """# tirOtir AI Academy - Morse Code Transmitter
MORSE_MAP = {
    'A': '.-', 'B': '-...', 'C': '-.-.', 'D': '-..', 'E': '.',
    'F': '..-.', 'G': '--.', 'H': '....', 'I': '..', 'J': '.---',
    'K': '-.-', 'L': '.-..', 'M': '--', 'N': '-.', 'O': '---',
    'P': '.--.', 'Q': '--.-', 'R': '.-.', 'S': '...', 'T': '-',
    'U': '..-', 'V': '...-', 'W': '.--', 'X': '-..-', 'Y': '-.--',
    'Z': '--..', '1': '.----', '2': '..---', '3': '...--',
    '4': '....-', '5': '.....', '6': '-....', '7': '--...',
    '8': '---..', '9': '----.', '0': '-----', ' ': '/'
}

def text_to_morse(text):
    return ' '.join(MORSE_MAP.get(char.upper(), '?') for char in text)

message = "TIROTIR PYTHON"
print("Plaintext:", message)
print("Morse Code:", text_to_morse(message))
"""
      ),
      ProjectItem(
          id = "guess",
          title = "Number Guessing Game",
          description = "Binary search challenge where player guesses a hidden number between 1 and 100 with dynamic hint feedback.",
          category = "Games & Logic",
          iconName = "Casino",
          sourceCode = """# tirOtir AI Academy - Number Guessing Challenge
import random

target = random.randint(1, 100)
attempts = 0

def make_guess(guess, target):
    global attempts
    attempts += 1
    if guess < target:
        return "Too low! Aim higher."
    elif guess > target:
        return "Too high! Aim lower."
    else:
        return f"Correct! You found it in {attempts} attempts."

print("Game initialized. Range: 1 to 100")
print("First guess: 50 ->", make_guess(50, target))
"""
      ),
      ProjectItem(
          id = "passgen",
          title = "Password Security Suite",
          description = "Generates cryptographically random passwords and calculates entropy, length, and strength metrics.",
          category = "Cybersecurity",
          iconName = "Security",
          sourceCode = """# tirOtir AI Academy - Password Generator & Strength Checker
import random
import string

def generate_password(length=14, use_symbols=True):
    chars = string.ascii_letters + string.digits
    if use_symbols:
        chars += "!@#$%^&*()-_=+"
    return ''.join(random.choice(chars) for _ in range(length))

def check_strength(pwd):
    score = 0
    if len(pwd) >= 8: score += 1
    if len(pwd) >= 12: score += 1
    if any(c.isupper() for c in pwd): score += 1
    if any(c.isdigit() for c in pwd): score += 1
    if any(c in "!@#$%^&*()-_=+" for c in pwd): score += 1
    
    levels = ["Weak", "Fair", "Good", "Strong", "Military-Grade"]
    return levels[min(score, 4)]

pw = generate_password(16)
print("Generated Password:", pw)
print("Security Rating:", check_strength(pw))
"""
      ),
      ProjectItem(
          id = "rps",
          title = "Rock, Paper, Scissors",
          description = "Interactive match against the tirOtir Python AI bot with round tracking, score keeper, and win probability.",
          category = "Games & AI",
          iconName = "SportsEsports",
          sourceCode = """# tirOtir AI Academy - Rock Paper Scissors Bot
import random

CHOICES = ["rock", "paper", "scissors"]

def play_round(user_choice):
    ai_choice = random.choice(CHOICES)
    if user_choice == ai_choice:
        outcome = "Tie"
    elif (user_choice == "rock" and ai_choice == "scissors") or \
         (user_choice == "paper" and ai_choice == "rock") or \
         (user_choice == "scissors" and ai_choice == "paper"):
        outcome = "User Wins"
    else:
        outcome = "AI Wins"
    return f"User: {user_choice} | AI: {ai_choice} => {outcome}"

print(play_round("rock"))
"""
      ),
      ProjectItem(
          id = "todo",
          title = "Terminal Task Manager",
          description = "CLI simulation to add, toggle, delete, and inspect tasks with priorities and completion flags.",
          category = "Productivity",
          iconName = "Checklist",
          sourceCode = """# tirOtir AI Academy - To-Do Task Manager
tasks = [
    {"id": 1, "title": "Review Python Lesson 6", "done": True},
    {"id": 2, "title": "Run Turtle Graphics Spiral", "done": False},
    {"id": 3, "title": "Pass Milestone 10 Quiz", "done": False},
]

def show_tasks():
    print("--- Current Task List ---")
    for t in tasks:
        status = "[x]" if t["done"] else "[ ]"
        print(f"{status} #{t['id']}: {t['title']}")

def add_task(title):
    new_id = len(tasks) + 1
    tasks.append({"id": new_id, "title": title, "done": False})
    print(f"Added task: {title}")

show_tasks()
add_task("Complete Capstone Project")
show_tasks()
"""
      ),
      ProjectItem(
          id = "kgunit",
          title = "Kg to Units Converter",
          description = "Official tirOtir curriculum converter (#izehkgcmd / #izehkggtk) converting kg to grams, pounds, ounces, and stones.",
          category = "Curriculum Classic",
          iconName = "Scale",
          sourceCode = """# tirOtir Official Snippet (#izehkgcmd)
def convert_kg_to_units(kg):
    grams = kg * 1000.0
    pounds = kg * 2.20462
    ounces = kg * 35.274
    stones = kg * 0.15747
    return grams, pounds, ounces, stones

kg_input = 10.0
g, lbs, oz, st = convert_kg_to_units(kg_input)
print(f"Input: {kg_input} kg")
print(f"Grams:   {g:.2f} g")
print(f"Pounds:  {lbs:.2f} lbs")
print(f"Ounces:  {oz:.2f} oz")
print(f"Stones:  {st:.2f} stones")
"""
      )
  )
}
