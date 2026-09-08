package com.example.data

data class Lesson(
    val id: String,
    val weekNumber: Int,
    val title: String,
    val summary: String,
    val conceptualExplanation: String,
    val syntaxBreakdown: String,
    val runnableCode: String,
    val extraExampleTitle: String,
    val extraExampleCode: String,
    val exerciseQuestion: String,
    val exerciseHint: String,
    val exerciseSolution: String
)

data class WeekModule(
    val weekNumber: Int,
    val title: String,
    val subtitle: String,
    val badge: String,
    val lessons: List<Lesson>
)

object CurriculumData {
  val weeks: List<WeekModule> = listOf(
      WeekModule(
          weekNumber = 1,
          title = "Week 1: Python Introduction & Output",
          subtitle = "Interpreter, syntax, printing, and comments",
          badge = "Fundamentals",
          lessons = listOf(
              Lesson(
                  id = "w1_l1",
                  weekNumber = 1,
                  title = "Hello World & Printing",
                  summary = "Learn how Python interprets instructions and outputs text to the standard console.",
                  conceptualExplanation = "Python is a high-level, interpreted programming language known for readable syntax. The print() function transmits data to the system console output stream. Strings must be enclosed in single quotes ('...') or double quotes (\"...\").",
                  syntaxBreakdown = "print(*objects, sep=' ', end='\\n')\n# Single-line comment\n\"\"\" Multi-line docstring \"\"\"",
                  runnableCode = """# tirOtir AI Academy - Lesson 1
print("Hello, World!")
print("Welcome to tirOtir Python!")
print("Programming is thinking with precision.")
""".trimIndent(),
                  extraExampleTitle = "ASCII Art & Fun Output (tirOtir #izeh)",
                  extraExampleCode = """# tirOtir Snippet: ASCII Fish Art
print("Hello, Python!")
print("Learning at tirOtir AI Academy")
fish = '''
><(((('>
'''
print(fish)
""".trimIndent(),
                  exerciseQuestion = "Write code to print 'Python 3.12 Rocks!' on line 1 and your favorite programming goal on line 2.",
                  exerciseHint = "Use two print() statements or '\\n' newline character.",
                  exerciseSolution = """print("Python 3.12 Rocks!\nMy goal is mastering AI & Python with tirOtir!")"""
              ),
              Lesson(
                  id = "w1_l2",
                  weekNumber = 1,
                  title = "Multi-line Strings & Escape Characters",
                  summary = "Format output using newline (\\n), tab (\\t), and quotes escaping.",
                  conceptualExplanation = "Escape sequences begin with a backslash (\\) and allow special character formatting like newlines (\\n) and tabs (\\t). Triple-quoted strings preserve line breaks exactly as typed.",
                  syntaxBreakdown = "\\n : Newline\n\\t : Tab\n\\\" : Escaped double quote\n''' or \"\"\" : Triple-quoted multi-line string",
                  runnableCode = """print("Line 1\nLine 2\n\tIndented Column")
info = '''
tirOtir AI Academy
Accredited TVTO Center
Location: Izeh / Global Online
'''
print(info)
""".trimIndent(),
                  extraExampleTitle = "Escape Sequence Practice",
                  extraExampleCode = """print("Python\'s simplicity is powerful.")
print("Path:\tC:\\Python\\Projects")
""".trimIndent(),
                  exerciseQuestion = "Print a 3x3 grid of asterisks (*) separated by tabs.",
                  exerciseHint = "Use \\t between asterisks on each row.",
                  exerciseSolution = """print("*\t*\t*\n*\t*\t*\n*\t*\t*")"""
              )
          )
      ),
      WeekModule(
          weekNumber = 2,
          title = "Week 2: Variables & Data Types",
          subtitle = "Primitive types, type conversions, and input()",
          badge = "Memory & Types",
          lessons = listOf(
              Lesson(
                  id = "w2_l1",
                  weekNumber = 2,
                  title = "Variables & Core Primitives",
                  summary = "Master integers, floats, strings, booleans, and variable assignments.",
                  conceptualExplanation = "In Python, variables are dynamically typed references to objects in memory. The core primitives are int (integers), float (decimals), str (text), and bool (True/False).",
                  syntaxBreakdown = "variable_name = value\ntype(obj) -> returns data type\nint(x), float(x), str(x), bool(x) -> type casting",
                  runnableCode = """name = "tirOtir Learner"
course_weeks = 16
progress_percent = 98.5
is_enrolled = True

print("Student:", name)
print("Duration:", course_weeks, "weeks")
print("Progress:", progress_percent, "%")
print("Enrolled:", is_enrolled)
""".trimIndent(),
                  extraExampleTitle = "Dynamic Typing & Re-assignment",
                  extraExampleCode = """x = 42
print("x as int:", x, type(x))
x = "Now I am a string"
print("x as str:", x, type(x))
""".trimIndent(),
                  exerciseQuestion = "Create variables for radius=7 and pi=3.14159, compute area = pi * (radius ** 2), and print it.",
                  exerciseHint = "Use the exponent operator **.",
                  exerciseSolution = """radius = 7
pi = 3.14159
area = pi * (radius ** 2)
print("Circle Area:", area)"""
              ),
              Lesson(
                  id = "w2_l2",
                  weekNumber = 2,
                  title = "Interactive User Input: input()",
                  summary = "Capture user input from the console and convert to appropriate types.",
                  conceptualExplanation = "The input() function pauses program execution and captures keyboard input as a string (str). When numbers are needed, wrap input() in int() or float().",
                  syntaxBreakdown = "user_text = input('Enter prompt: ')\nuser_number = int(input('Enter age: '))\nuser_float = float(input('Enter price: '))",
                  runnableCode = """# Interactive input demo
name = input("What is your name? ")
year_str = input("What year is it? ")
year = int(year_str)
print("Welcome to tirOtir Academy, " + name + "!")
print("Next year will be:", year + 1)
""".trimIndent(),
                  extraExampleTitle = "Input Type Conversion",
                  extraExampleCode = """kg_text = input("Enter weight in kg: ")
kg = float(kg_text)
grams = kg * 1000
print("Equivalent in grams:", grams)
""".trimIndent(),
                  exerciseQuestion = "Write a program that asks for two integers from the user and prints their product.",
                  exerciseHint = "Convert both inputs with int() before multiplying.",
                  exerciseSolution = """a = int(input("Enter first number: "))
b = int(input("Enter second number: "))
print("Product:", a * b)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 3,
          title = "Week 3: Operators & Expressions",
          subtitle = "Arithmetic, comparison, and logical operators",
          badge = "Expressions",
          lessons = listOf(
              Lesson(
                  id = "w3_l1",
                  weekNumber = 3,
                  title = "Arithmetic Operators & Math",
                  summary = "Addition, subtraction, multiplication, division, floor division, modulo, and powers.",
                  conceptualExplanation = "Python includes + (add), - (subtract), * (multiply), / (float divide), // (integer floor divide), % (modulo remainder), and ** (power). Operator precedence follows PEMDAS.",
                  syntaxBreakdown = "+, -, *, /, //, %, **\nCompound assignments: +=, -=, *=, /=",
                  runnableCode = """a = 15
b = 4

print("Sum:", a + b)
print("Difference:", a - b)
print("Product:", a * b)
print("Float Division:", a / b)
print("Floor Division:", a // b)
print("Remainder (Modulo):", a % b)
print("Power (15^2):", a ** 2)
""".trimIndent(),
                  extraExampleTitle = "tirOtir Addition & Formulas (#izehadd)",
                  extraExampleCode = """# Addition of numbers with f-strings
a = 15
b = 25
c = 10
sum_abc = a + b + c
print(f"Total Sum: {sum_abc}")
avg = sum_abc / 3
print(f"Average: {avg:.2f}")
""".trimIndent(),
                  exerciseQuestion = "Calculate the remainder of 250 divided by 17, and also 2 to the power of 10.",
                  exerciseHint = "Use % and **.",
                  exerciseSolution = """print("250 % 17 =", 250 % 17)
print("2 ** 10 =", 2 ** 10)"""
              ),
              Lesson(
                  id = "w3_l2",
                  weekNumber = 3,
                  title = "Comparison & Logical Operators",
                  summary = "Equality, inequality, ordering, and boolean logic (and, or, not).",
                  conceptualExplanation = "Comparison operators (==, !=, <, <=, >, >=) return boolean True or False. Logical operators (and, or, not) chain multiple truth evaluations together.",
                  syntaxBreakdown = "== : equal to\n!= : not equal\n<, <=, >, >=\nand : True if both true\nor : True if either is true\nnot : inverts boolean",
                  runnableCode = """age = 20
has_id = True

can_enter = (age >= 18) and has_id
print("Age:", age)
print("Can enter venue?", can_enter)

score = 85
is_passing = score >= 60 and not (score < 0)
print("Score passing?", is_passing)
""".trimIndent(),
                  extraExampleTitle = "Chained Comparisons in Python",
                  extraExampleCode = """x = 15
# Python allows clean mathematical chaining:
in_range = 10 <= x <= 20
print("Is 15 between 10 and 20?", in_range)
""".trimIndent(),
                  exerciseQuestion = "Test whether a number n=42 is even AND greater than 40.",
                  exerciseHint = "(n % 2 == 0) and (n > 40).",
                  exerciseSolution = """n = 42
is_even_and_large = (n % 2 == 0) and (n > 40)
print("Result:", is_even_and_large)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 4,
          title = "Week 4: Conditional Control Flow",
          subtitle = "if, elif, else branches and nested conditions",
          badge = "Decision Making",
          lessons = listOf(
              Lesson(
                  id = "w4_l1",
                  weekNumber = 4,
                  title = "if, elif, and else Statements",
                  summary = "Direct program flow dynamically based on evaluated conditions.",
                  conceptualExplanation = "Conditional blocks execute specific code suites only when their boolean expression is truthy. Indentation (4 spaces) delineates code blocks in Python.",
                  syntaxBreakdown = "if condition:\n    pass\nelif other_condition:\n    pass\nelse:\n    pass",
                  runnableCode = """# tirOtir Snippet: If Statement (#izehif)
score = 88

if score >= 90:
    grade = "A (Excellence)"
elif score >= 80:
    grade = "B (Very Good)"
elif score >= 70:
    grade = "C (Good)"
elif score >= 60:
    grade = "D (Passing)"
else:
    grade = "F (Needs Improvement)"

print("Final Grade:", grade)
""".trimIndent(),
                  extraExampleTitle = "Number Polarity Evaluator",
                  extraExampleCode = """num = -7
if num > 0:
    print("The number is positive")
elif num < 0:
    print("The number is negative")
else:
    print("The number is zero")
""".trimIndent(),
                  exerciseQuestion = "Write an if-elif-else block to classify a temperature: above 30 is 'Hot', between 18 and 30 is 'Comfortable', below 18 is 'Chilly'.",
                  exerciseHint = "Check temp > 30, temp >= 18, else.",
                  exerciseSolution = """temp = 22
if temp > 30:
    print("Hot")
elif temp >= 18:
    print("Comfortable")
else:
    print("Chilly")"""
              )
          )
      ),
      WeekModule(
          weekNumber = 5,
          title = "Week 5: Iteration & Loops",
          subtitle = "for in range, while loops, break, and continue",
          badge = "Loops",
          lessons = listOf(
              Lesson(
                  id = "w5_l1",
                  weekNumber = 5,
                  title = "for Loops & range()",
                  summary = "Iterate over arithmetic sequences and collections with for loops.",
                  conceptualExplanation = "The for loop repeats a block of code for each element in an iterable. The range(start, stop, step) generator yields integers from start up to (but not including) stop.",
                  syntaxBreakdown = "for i in range(5):          # 0, 1, 2, 3, 4\nfor i in range(1, 10, 2):   # 1, 3, 5, 7, 9\nfor i in range(5, 0, -1):   # 5, 4, 3, 2, 1",
                  runnableCode = """# tirOtir for loop demonstration (#izehfor)
print("Counting 1 to 5:")
for i in range(1, 6):
    print("Step", i)

print("\nEven numbers up to 10:")
for n in range(2, 11, 2):
    print(n, end=" ")
print()
""".trimIndent(),
                  extraExampleTitle = "Summation with for loop",
                  extraExampleCode = """total = 0
for x in range(1, 11):
    total += x
print("Sum of numbers 1 to 10 is:", total)
""".trimIndent(),
                  exerciseQuestion = "Print the square of all odd numbers from 1 to 9.",
                  exerciseHint = "range(1, 10, 2) and i ** 2.",
                  exerciseSolution = """for i in range(1, 10, 2):
    print(i, "squared is", i ** 2)"""
              ),
              Lesson(
                  id = "w5_l2",
                  weekNumber = 5,
                  title = "while Loops, break, and continue",
                  summary = "Execute code while a condition holds true, with break & continue controls.",
                  conceptualExplanation = "A while loop continues executing as long as its condition remains True. Use 'break' to exit immediately, and 'continue' to skip the rest of the current iteration.",
                  syntaxBreakdown = "while condition:\n    if stop_cond: break\n    if skip_cond: continue\n    count += 1",
                  runnableCode = """# tirOtir while loop countdown (#izehwhile)
count = 5
print("Rocket launch countdown:")
while count > 0:
    print(count)
    count -= 1
print("Blast off! 🚀")
""".trimIndent(),
                  extraExampleTitle = "Factorial calculation via while",
                  extraExampleCode = """n = 5
fact = 1
i = 1
while i <= n:
    fact *= i
    i += 1
print("Factorial of 5 is:", fact)
""".trimIndent(),
                  exerciseQuestion = "Use a while loop to print powers of 2 (1, 2, 4, 8, 16...) less than 100.",
                  exerciseHint = "Start at val=1, print val, multiply by 2.",
                  exerciseSolution = """val = 1
while val < 100:
    print(val, end=" ")
    val *= 2
print()"""
              )
          )
      ),
      WeekModule(
          weekNumber = 6,
          title = "Week 6: Lists & Comprehensions",
          subtitle = "Indexing, slicing, mutating, and list comprehensions",
          badge = "Data Structures",
          lessons = listOf(
              Lesson(
                  id = "w6_l1",
                  weekNumber = 6,
                  title = "Lists Operations & Methods",
                  summary = "Create, index, slice, and manipulate mutable ordered sequences.",
                  conceptualExplanation = "Lists are ordered, mutable sequences that can hold heterogeneous items. Indexing starts at 0. Negative indices count from the end (-1 is last).",
                  syntaxBreakdown = "lst = [1, 2, 3]\nlst.append(x)\nlst.remove(x)\nlst.pop()\nlen(lst)\nlst[start:end:step]",
                  runnableCode = """# tirOtir List Operations (#izehlistops)
fruits = ["Apple", "Banana", "Cherry"]
fruits.append("Orange")
fruits.append("Date")

print("List items:", fruits)
print("Length:", len(fruits))
print("First item:", fruits[0])
print("Last item:", fruits[-1])
print("Slice [1:4]:", fruits[1:4])

fruits.remove("Banana")
print("After removing Banana:", fruits)
""".trimIndent(),
                  extraExampleTitle = "Sorting and List Math",
                  extraExampleCode = """nums = [45, 12, 89, 3, 27]
print("Sum:", sum(nums))
print("Min:", min(nums))
print("Max:", max(nums))
nums.sort()
print("Sorted ascending:", nums)
""".trimIndent(),
                  exerciseQuestion = "Create a list of 5 numbers, sort them in descending order, and print the largest two.",
                  exerciseHint = "Use sort(reverse=True) and slicing [:2].",
                  exerciseSolution = """nums = [14, 88, 2, 99, 43]
nums.sort(reverse=True)
print("Top two:", nums[:2])"""
              ),
              Lesson(
                  id = "w6_l2",
                  weekNumber = 6,
                  title = "List Comprehensions",
                  summary = "Construct new lists elegantly using concise comprehension syntax.",
                  conceptualExplanation = "List comprehensions offer a succinct syntax to create a new list based on existing iterables: [expression for item in iterable if condition].",
                  syntaxBreakdown = "[expr for item in iterable]\n[expr for item in iterable if condition]",
                  runnableCode = """# Squares of numbers 1 to 8
squares = [x ** 2 for x in range(1, 9)]
print("Squares:", squares)

# Filter only even squares
even_squares = [s for s in squares if s % 2 == 0]
print("Even Squares:", even_squares)

# Transform text
languages = ["python", "kotlin", "javascript"]
caps = [lang.upper() for lang in languages]
print("Uppercase:", caps)
""".trimIndent(),
                  extraExampleTitle = "Unit conversion with comprehensions (#izehkgcmd)",
                  extraExampleCode = """# Converting list of weights (kg) to grams
weights_kg = [1.5, 3.0, 0.75, 5.2]
weights_g = [k * 1000 for k in weights_kg]
print("Weights in Grams:", weights_g)
""".trimIndent(),
                  exerciseQuestion = "Given numbers = [1, 2, 3, 4, 5, 6, 7, 8], produce a list of cubes of only the odd numbers.",
                  exerciseHint = "[n**3 for n in numbers if n % 2 != 0]",
                  exerciseSolution = """numbers = [1, 2, 3, 4, 5, 6, 7, 8]
odd_cubes = [n ** 3 for n in numbers if n % 2 != 0]
print("Odd cubes:", odd_cubes)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 7,
          title = "Week 7: Tuples & Sets",
          subtitle = "Immutability, set theory operations, and uniqueness",
          badge = "Collections",
          lessons = listOf(
              Lesson(
                  id = "w7_l1",
                  weekNumber = 7,
                  title = "Tuples & Immutability",
                  summary = "Store immutable ordered collections and unpack multiple values.",
                  conceptualExplanation = "Tuples are ordered collections created with parentheses (a, b, c) that cannot be modified after creation. They are faster than lists and serve as reliable fixed records.",
                  syntaxBreakdown = "point = (10, 20)\nx, y = point  # Tuple unpacking\ncoords = (lat, lon, alt)",
                  runnableCode = """# Geographic coordinates of Izeh Academy
location = ("Izeh", 31.8344, 49.8689)
city, lat, lon = location

print(f"City: {city}")
print(f"Latitude: {lat}° N")
print(f"Longitude: {lon}° E")
""".trimIndent(),
                  extraExampleTitle = "Returning Multiple Values",
                  extraExampleCode = """def min_max(numbers):
    return min(numbers), max(numbers)

low, high = min_max([12, 45, 2, 99, 31])
print("Lowest:", low, "| Highest:", high)
""".trimIndent(),
                  exerciseQuestion = "Create a tuple with 4 elements, unpack the first two into variables a and b, and print their sum.",
                  exerciseHint = "t = (10, 20, 30, 40); a, b = t[0], t[1].",
                  exerciseSolution = """t = (15, 35, 50, 100)
a, b = t[0], t[1]
print("Sum:", a + b)"""
              ),
              Lesson(
                  id = "w7_l2",
                  weekNumber = 7,
                  title = "Sets & Mathematical Set Operations",
                  summary = "Store unique items and compute union, intersection, and difference.",
                  conceptualExplanation = "Sets are unordered collections of unique elements. Duplicates are automatically removed. Sets support mathematical operations: union (|), intersection (&), and difference (-).",
                  syntaxBreakdown = "s = {1, 2, 3}\ns.add(4)\ns.remove(x)\ns1 | s2  # Union\ns1 & s2  # Intersection\ns1 - s2  # Difference",
                  runnableCode = """python_students = {"Sara", "Ali", "Reza", "Elena"}
ai_students = {"Elena", "Kian", "Sara", "Nima"}

all_students = python_students | ai_students
both_courses = python_students & ai_students
python_only = python_students - ai_students

print("All enrolled:", all_students)
print("Dual enrollment:", both_courses)
print("Python only:", python_only)
""".trimIndent(),
                  extraExampleTitle = "De-duplicating a List with Set",
                  extraExampleCode = """raw_tags = ["python", "ai", "python", "code", "ai", "data"]
unique_tags = list(set(raw_tags))
print("Unique tags:", unique_tags)
""".trimIndent(),
                  exerciseQuestion = "Given set A={1, 2, 3, 4} and set B={3, 4, 5, 6}, find items present in either set but NOT both (symmetric difference).",
                  exerciseHint = "Use the symmetric difference operator ^.",
                  exerciseSolution = """A = {1, 2, 3, 4}
B = {3, 4, 5, 6}
print("Symmetric difference:", A ^ B)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 8,
          title = "Week 8: Dictionaries & Key-Value Mapping",
          subtitle = "Associative arrays, keys, values, and nesting",
          badge = "Mappings",
          lessons = listOf(
              Lesson(
                  id = "w8_l1",
                  weekNumber = 8,
                  title = "Dictionary Fundamentals (#izehdict)",
                  summary = "Store and access associative key-value data with instant lookup.",
                  conceptualExplanation = "A dictionary maps unique keys to values using curly braces {key: value}. Keys must be immutable types (strings, numbers, tuples). Values can be any data type.",
                  syntaxBreakdown = "d = {'key': 'value'}\nd['new_key'] = 'val'\nd.get('key', default)\ndel d['key']\nd.keys(), d.values(), d.items()",
                  runnableCode = """# tirOtir Dictionary Operations (#izehdict)
academy = {
    "name": "tirOtir AI Academy",
    "course": "Python Masterclass",
    "students": 140,
    "city": "Izeh",
    "accredited": True
}

print("Academy Name:", academy["name"])
print("Course:", academy["course"])

# Add new key-value pair
academy["duration_weeks"] = 16
print("All keys:", list(academy.keys()))

# Check key membership
if "accredited" in academy:
    print("Institution is TVTO Accredited!")
""".trimIndent(),
                  extraExampleTitle = "Dictionary Iteration",
                  extraExampleCode = """grades = {"Alice": 95, "Bob": 88, "Charlie": 92}
for student, score in grades.items():
    print(f"{student}: {score} points")
""".trimIndent(),
                  exerciseQuestion = "Write code to count the frequency of each word in the sentence 'python is easy and python is fast'.",
                  exerciseHint = "Split by space and loop over words updating a dictionary.",
                  exerciseSolution = """text = "python is easy and python is fast"
words = text.split()
counts = {}
for w in words:
    counts[w] = counts.get(w, 0) + 1
print("Word Counts:", counts)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 9,
          title = "Week 9: Functions & Modular Code",
          subtitle = "def, arguments, parameters, return, and defaults",
          badge = "Modularization",
          lessons = listOf(
              Lesson(
                  id = "w9_l1",
                  weekNumber = 9,
                  title = "Defining Functions (#izehfuncdef)",
                  summary = "Decompose problems into reusable, clean, and testable functions.",
                  conceptualExplanation = "Functions are declared using the 'def' keyword, followed by a name, parameter list, and an indented code body. The 'return' statement passes data back to the caller.",
                  syntaxBreakdown = "def function_name(param1, param2=default):\n    # logic\n    return result",
                  runnableCode = """# tirOtir Function Definition (#izehfuncdef)
def greet(name, institution="tirOtir AI Academy"):
    return f"Hello {name}, welcome to {institution}!"

msg1 = greet("Maryam")
msg2 = greet("Kian", "Andisheh Pardazan Institute")

print(msg1)
print(msg2)
""".trimIndent(),
                  extraExampleTitle = "Unit Conversion Function (#izehkgcmd)",
                  extraExampleCode = """def convert_kg_to_units(kg):
    grams = kg * 1000
    pounds = kg * 2.20462
    ounces = kg * 35.274
    return grams, pounds, ounces

g, lbs, oz = convert_kg_to_units(5.0)
print(f"5 kg = {g} g | {lbs:.2f} lbs | {oz:.2f} oz")
""".trimIndent(),
                  exerciseQuestion = "Write a function celsius_to_fahrenheit(c) that returns (c * 9/5) + 32, and test it with 25°C.",
                  exerciseHint = "Define the function and return the computed expression.",
                  exerciseSolution = """def celsius_to_fahrenheit(c):
    return (c * 9 / 5) + 32

print("25°C in Fahrenheit:", celsius_to_fahrenheit(25))"""
              )
          )
      ),
      WeekModule(
          weekNumber = 10,
          title = "Week 10: Scope, Lambdas & Built-ins",
          subtitle = "Local vs global scope, lambda expressions, map and filter",
          badge = "Functional",
          lessons = listOf(
              Lesson(
                  id = "w10_l1",
                  weekNumber = 10,
                  title = "Scope & Anonymous Lambdas",
                  summary = "Understand LEGB scope resolution and concise anonymous lambda functions.",
                  conceptualExplanation = "Python resolves names via LEGB (Local, Enclosing, Global, Built-in). Lambda expressions provide anonymous single-expression functions: lambda x: expr.",
                  syntaxBreakdown = "lambda x, y: x + y\nmap(func, iterable)\nfilter(func, iterable)",
                  runnableCode = """# Lambda functions
multiply = lambda a, b: a * b
square = lambda x: x ** 2

print("3 * 7 =", multiply(3, 7))
print("8 squared =", square(8))

numbers = [1, 2, 3, 4, 5, 6]
evens = list(filter(lambda n: n % 2 == 0, numbers))
doubled = list(map(lambda n: n * 2, numbers))

print("Evens:", evens)
print("Doubled:", doubled)
""".trimIndent(),
                  extraExampleTitle = "Sorting with Custom Lambda Keys",
                  extraExampleCode = """students = [("Ali", 85), ("Sara", 98), ("Reza", 91)]
# Sort by grade descending
sorted_by_grade = sorted(students, key=lambda s: s[1], reverse=True)
print("Leaderboard:", sorted_by_grade)
""".trimIndent(),
                  exerciseQuestion = "Use filter and a lambda to keep only words longer than 4 letters from ['ai', 'python', 'code', 'tirOtir', 'fun'].",
                  exerciseHint = "lambda w: len(w) > 4",
                  exerciseSolution = """words = ['ai', 'python', 'code', 'tirOtir', 'fun']
long_words = list(filter(lambda w: len(w) > 4, words))
print("Long words:", long_words)"""
              )
          )
      ),
      WeekModule(
          weekNumber = 11,
          title = "Week 11: File Input / Output (I/O)",
          subtitle = "Reading, writing, appending files, and context managers",
          badge = "Persistence",
          lessons = listOf(
              Lesson(
                  id = "w11_l1",
                  weekNumber = 11,
                  title = "File Operations (#izehreadfile, #izehwritefile)",
                  summary = "Read and write data reliably using Python's 'with open' context manager.",
                  conceptualExplanation = "The 'with open(filename, mode) as file:' idiom automatically handles resource cleanup. Modes: 'r' (read), 'w' (write/overwrite), 'a' (append).",
                  syntaxBreakdown = "with open('file.txt', 'w') as f:\n    f.write('text\\n')\nwith open('file.txt', 'r') as f:\n    content = f.read()",
                  runnableCode = """# Simulated File I/O workflow from tirOtir snippets
# In desktop Python:
code_snippet = '''with open('notes.txt', 'w') as file:
    file.write("tirOtir AI Academy\\n")
    file.write("Mastering Python Step-by-Step\\n")

with open('notes.txt', 'r') as file:
    for line in file:
        print(line.strip())
'''
print("Python File I/O Script:")
print(code_snippet)
""".trimIndent(),
                  extraExampleTitle = "Counting Words in a File (#izehreadfile)",
                  extraExampleCode = """sample_file_text = "Python is powerful\\nPython is easy to learn\\nWelcome to tirOtir"
words = sample_file_text.split()
print("Sample content:")
print(sample_file_text)
print(f"Total words counted: {len(words)}")
""".trimIndent(),
                  exerciseQuestion = "What mode should you pass to open() if you want to add content to the end of a file without erasing existing text?",
                  exerciseHint = "Look at append mode.",
                  exerciseSolution = """# Answer: 'a' mode (append)
# Example: with open('log.txt', 'a') as f: f.write('new entry\\n')"""
              )
          )
      ),
      WeekModule(
          weekNumber = 12,
          title = "Week 12: Exception Handling",
          subtitle = "try, except, finally, and robust defensive coding",
          badge = "Robustness",
          lessons = listOf(
              Lesson(
                  id = "w12_l1",
                  weekNumber = 12,
                  title = "try, except, and finally Blocks",
                  summary = "Prevent unexpected crashes by catching and recovering from runtime errors.",
                  conceptualExplanation = "Exceptions disrupt normal control flow when errors occur. Wrapping risky operations in try-except blocks allows graceful degradation.",
                  syntaxBreakdown = "try:\n    risky_operation()\nexcept ValueError as e:\n    handle_error(e)\nfinally:\n    cleanup()",
                  runnableCode = """# Handling Division and Type Errors
def safe_divide(a, b):
    try:
        result = a / b
        return result
    except ZeroDivisionError:
        return "Error: Cannot divide by zero!"
    except TypeError:
        return "Error: Both arguments must be numeric!"

print("10 / 2 =", safe_divide(10, 2))
print("10 / 0 =", safe_divide(10, 0))
print("10 / 'abc' =", safe_divide(10, "abc"))
""".trimIndent(),
                  extraExampleTitle = "File & Input Exception Handling",
                  extraExampleCode = """try:
    user_val = "forty-two"
    num = int(user_val)
except ValueError:
    print(f"Could not convert '{user_val}' into an integer!")
""".trimIndent(),
                  exerciseQuestion = "Write a function parse_int(text, default=0) that attempts to convert text to an integer and returns default if a ValueError occurs.",
                  exerciseHint = "Use try / except ValueError.",
                  exerciseSolution = """def parse_int(text, default=0):
    try:
        return int(text)
    except ValueError:
        return default

print(parse_int("123"))
print(parse_int("hello", -1))"""
              )
          )
      ),
      WeekModule(
          weekNumber = 13,
          title = "Week 13: Object-Oriented Programming",
          subtitle = "Classes, instances, __init__, and self",
          badge = "OOP",
          lessons = listOf(
              Lesson(
                  id = "w13_l1",
                  weekNumber = 13,
                  title = "Class & Object Foundations (#izehclassdef)",
                  summary = "Model real-world entities with attributes and behavior using classes.",
                  conceptualExplanation = "A class is a blueprint for creating objects. The __init__ constructor initializes instance attributes. The 'self' parameter refers to the current instance.",
                  syntaxBreakdown = "class ClassName:\n    def __init__(self, arg):\n        self.attribute = arg\n    def method(self):\n        return self.attribute",
                  runnableCode = """# tirOtir Class Definition (#izehclassdef)
class Student:
    def __init__(self, name, student_id, track):
        self.name = name
        self.student_id = student_id
        self.track = track
        self.completed_lessons = 0

    def complete_lesson(self):
        self.completed_lessons += 1
        return f"{self.name} completed lesson #{self.completed_lessons}"

    def get_info(self):
        return f"Student: {self.name} | Track: {self.track} | Lessons: {self.completed_lessons}"

s1 = Student("Ali", "TT-101", "Python & AI")
print(s1.complete_lesson())
print(s1.complete_lesson())
print(s1.get_info())
""".trimIndent(),
                  extraExampleTitle = "Bank Account OOP Model",
                  extraExampleCode = """class BankAccount:
    def __init__(self, owner, balance=0):
        self.owner = owner
        self.balance = balance

    def deposit(self, amount):
        self.balance += amount
        return self.balance

account = BankAccount("Sara", 500)
print("Balance after deposit:", account.deposit(250))
""".trimIndent(),
                  exerciseQuestion = "Create a Rectangle class with width and height attributes and an area() method.",
                  exerciseHint = "def area(self): return self.width * self.height",
                  exerciseSolution = """class Rectangle:
    def __init__(self, width, height):
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

rect = Rectangle(5, 8)
print("Area:", rect.area())"""
              )
          )
      ),
      WeekModule(
          weekNumber = 14,
          title = "Week 14: Inheritance & Polymorphism",
          subtitle = "Subclasses, super(), encapsulation, and polymorphism",
          badge = "Advanced OOP",
          lessons = listOf(
              Lesson(
                  id = "w14_l1",
                  weekNumber = 14,
                  title = "Inheritance & Method Overriding",
                  summary = "Reuse and specialize classes through hierarchical inheritance.",
                  conceptualExplanation = "Subclasses inherit attributes and methods from a parent class. The super() function invokes parent constructors and methods. Polymorphism allows uniform method interfaces across different subclasses.",
                  syntaxBreakdown = "class Child(Parent):\n    def __init__(self, a, b):\n        super().__init__(a)\n        self.b = b",
                  runnableCode = """# tirOtir Inheritance Example (#izehclassdef)
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def introduce(self):
        return f"Hello, I am {self.name}, {self.age} years old."

class Instructor(Person):
    def __init__(self, name, age, subject):
        super().__init__(name, age)
        self.subject = subject

    def introduce(self):
        return f"Greetings! I am {self.name}, instructor in {self.subject} at tirOtir Academy."

instructor = Instructor("Dr. Anzan", 38, "Applied AI")
print(instructor.introduce())
""".trimIndent(),
                  extraExampleTitle = "Polymorphism with Shape Classes",
                  extraExampleCode = """class Circle:
    def __init__(self, r): self.r = r
    def draw(self): return f"Drawing Circle radius {self.r}"

class Square:
    def __init__(self, s): self.s = s
    def draw(self): return f"Drawing Square side {self.s}"

shapes = [Circle(5), Square(10)]
for sh in shapes:
    print(sh.draw())
""".trimIndent(),
                  exerciseQuestion = "Create a Vehicle class with a move() method, and a Car subclass that overrides move() to return 'Driving on the highway'.",
                  exerciseHint = "Define class Car(Vehicle) and override def move(self).",
                  exerciseSolution = """class Vehicle:
    def move(self): return "Moving"

class Car(Vehicle):
    def move(self): return "Driving on the highway"

c = Car()
print(c.move())"""
              )
          )
      ),
      WeekModule(
          weekNumber = 15,
          title = "Week 15: Standard Library & Modules",
          subtitle = "math, random, datetime, json, and graphics modules",
          badge = "Ecosystem",
          lessons = listOf(
              Lesson(
                  id = "w15_l1",
                  weekNumber = 15,
                  title = "Python Batteries-Included Modules",
                  summary = "Harness math, random, json, datetime, and popular community libraries.",
                  conceptualExplanation = "Python includes a comprehensive standard library. Import modules with 'import module' or 'from module import item'. Modules like math, random, and json handle core computing tasks.",
                  syntaxBreakdown = "import math\nimport random\nimport json\nfrom datetime import datetime",
                  runnableCode = """import math
import random

# Math operations
print("Pi:", round(math.pi, 4))
print("Square root of 144:", math.isqrt(144))
print("Cosine of 0:", math.cos(0))

# Random generation
lucky_number = random.randint(1, 100)
colors = ["Navy", "Gold", "Emerald", "Cyan"]
choice = random.choice(colors)

print(f"Generated number: {lucky_number}")
print(f"Random color pick: {choice}")
""".trimIndent(),
                  extraExampleTitle = "Modules in tirOtir Curriculum (#izehturtule, #izehcolorama)",
                  extraExampleCode = """# Modules explored in tirOtir Academy:
# 1. turtle: 2D procedural vector graphics
# 2. colorama / termcolor: Terminal color styling
# 3. pyttsx3 / gTTS: Text-to-speech audio engines
# 4. pillow (PIL): Image manipulation
# 5. tkinter / tkintermapview: Desktop GUI windows & maps
print("tirOtir curriculum incorporates interactive GUI & Graphics modules!")
""".trimIndent(),
                  exerciseQuestion = "Use math.ceil and math.floor to round 7.25 up and down respectively.",
                  exerciseHint = "math.ceil(7.25) and math.floor(7.25)",
                  exerciseSolution = """import math
val = 7.25
print("Ceil:", math.ceil(val))
print("Floor:", math.floor(val))"""
              )
          )
      ),
      WeekModule(
          weekNumber = 16,
          title = "Week 16: Capstone Projects & Packaging",
          subtitle = "PyInstaller, desktop GUIs, and production practices",
          badge = "Capstone",
          lessons = listOf(
              Lesson(
                  id = "w16_l1",
                  weekNumber = 16,
                  title = "PyInstaller & Desktop Packaging (#izehpyinstaller)",
                  summary = "Bundle Python scripts into standalone executables for Windows, Mac, and Linux.",
                  conceptualExplanation = "PyInstaller packages Python applications and all their dependencies into a single standalone executable package (.exe or binary), allowing users to run scripts without installing Python.",
                  syntaxBreakdown = "pip install pyinstaller\npyinstaller --onefile --windowed main.py",
                  runnableCode = """# Packaging workflow from tirOtir Academy (#izehpyinstaller)
steps = [
    "1. Write and test your Python program",
    "2. Install packaging tool: pip install pyinstaller",
    "3. Build executable: pyinstaller --onefile --windowed app.py",
    "4. Locate your binary in the 'dist' directory",
    "5. Distribute your professional standalone application!"
]
for step in steps:
    print(step)
""".trimIndent(),
                  extraExampleTitle = "Tkinter Desktop GUI Structure (#izehtk, #izehkggtk)",
                  extraExampleCode = """# Structure of desktop GUI apps in tirOtir curriculum
print('''import tkinter as tk

root = tk.Tk()
root.title("tirOtir Python App")
root.geometry("400x300")

label = tk.Label(root, text="Welcome to tirOtir Desktop Suite")
label.pack(pady=20)

root.mainloop()''')
""".trimIndent(),
                  exerciseQuestion = "What flag in PyInstaller is used to bundle all libraries into a single file?",
                  exerciseHint = "--onefile",
                  exerciseSolution = """# Answer: --onefile
# Command: pyinstaller --onefile your_script.py"""
              )
          )
      )
  )
}
