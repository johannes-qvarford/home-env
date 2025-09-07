# Load environment variables from .env file using bass
if test -f ~/.env
    bass source ~/.env
end
set -gx OPENAI_API_KEY $OPENROUTER_KEY