
import os
from flask import Flask, request, jsonify, render_template
import google.generativeai as gen_ai
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Set up Google Gemini-Pro AI model
GOOGLE_API_KEY = "AIzaSyAQ313Y5CKYfHCcEVyN8QdHtWMHZUVrkG8"
gen_ai.configure(api_key=GOOGLE_API_KEY)
model = gen_ai.GenerativeModel('gemini-pro')

# Initialize chat session
chat_session = model.start_chat(history=[])

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/chat', methods=['POST'])
def chat():
    user_prompt = request.json.get('message')
    if user_prompt:
        gemini_response = chat_session.send_message(user_prompt)
        return jsonify({"response": gemini_response.text})
    return jsonify({"response": "No message received."})

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8081))  # Use port from environment or default to 5001
    app.run(debug=True, port=port)