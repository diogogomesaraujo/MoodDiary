import logging
from transformers import AutoModelForSequenceClassification, AutoTokenizer
from flask import Flask, request, jsonify
import torch
import os

# Use the emotion classification model
model_name = "j-hartmann/emotion-english-distilroberta-base"

# Load the model and tokenizer
model = AutoModelForSequenceClassification.from_pretrained(model_name)
tokenizer = AutoTokenizer.from_pretrained(model_name)

# Define the emotion labels (corresponding to the model's output)
emotion_labels = ['anger', 'disgust', 'fear', 'joy', 'neutral', 'sadness', 'surprise']

# Create a Flask web server
app = Flask(__name__)

@app.route('/emotion', methods=['POST'])
def detect_emotion():
    # Receive the text through the POST request
    text = request.get_json().get('text', '')

    # Tokenize and encode the input text
    inputs = tokenizer.encode_plus(
        text,
        add_special_tokens=True,
        max_length=512,
        padding='max_length',
        return_attention_mask=True,
        return_tensors='pt'
    )

    # Move inputs to the same device as the model
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    model.to(device)
    inputs = {key: value.to(device) for key, value in inputs.items()}

    # Use the model to analyze the text and determine the most prominent emotion
    with torch.no_grad():
        outputs = model(**inputs)
    emotions = outputs.logits.argmax(-1).item()

    # Get the predicted emotion label
    emotion = emotion_labels[emotions]

    # Return the detected emotion as a JSON response
    return jsonify({'emotion': emotion})

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(debug=True, host='0.0.0.0', port=port)