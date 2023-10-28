import openai
import os


def gpt(prompt):
    # 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
    openai.api_key = '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'
    
    messages = []
    messages.append(
            {"role": "user", "content": prompt},
        )
    chat = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", messages=messages
        )
    reply=chat.choices[0].message.content
    return reply

def test(idk):
    return "YES"

def test2(idk):
    pass

if __name__ == "__main__":
    message = "Generate me a 25 question quiz for MATH 214 at the University of Alberta in the following format: question1&q&answer1&q&answer2&q&answer3&q&answer4&f&question2&q&answer1&q&answer2&q&answer3&q&answer4&f&question3&q&answer1&q&answer2&q&answer3&q&answer4&f&question4&q&answer1&q&answer2&q&answer3&q&answer4"
    print(gpt(message))
    