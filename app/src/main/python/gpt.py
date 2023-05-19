import openai
import os


def generateContent(prompt):
    # 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
    openai.api_key = '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'
    message = "Generate me a 25 question quiz for MATH 214 at the University of Alberta in the following format: 1&question&answer1&answer2&answer3&answer4$2&question&answer1&answer2&answer3&answer4$3&question&answer1&answer2&answer3&answer4$4&question&answer1&answer2&answer3&answer4$"
    "Generate me a 25 question quiz for MATH 214 at the University of Alberta in the following format: 1&What is the meaning of life?&who knows&42&find it&yes&all of the above$2&What is 2 + 3?&1&4&8&5$"
    messages = []
    messages.append(
            {"role": "user", "content": prompt},
        )
    chat = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", messages=messages
        )
    reply=chat.choices[0].message.content
    return reply
