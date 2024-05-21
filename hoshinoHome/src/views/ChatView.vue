<template>
  <div class="flex flex-col mx-auto" style="height: 430px; max-width: 400px">
    <div
      id="messages"
      class="flex flex-col space-y-4 p-3 overflow-y-auto scrollbar-thumb-blue scrollbar-thumb-rounded scrollbar-track-blue-lighter scrollbar-w-2 scrolling-touch flex-1"
    >
      <template v-for="(message, index) in messages" :key="index">
        <div :class="message.from === 'bot' ? 'flex items-start' : 'flex items-end justify-end'">
          <div
            class="flex flex-col space-y-2 text-md leading-tight max-w-xs mx-2"
            :class="message.from === 'bot' ? 'order-2 items-start' : 'order-1 items-end'"
          >
            <div>
              <span
                class="px-4 py-3 rounded-xl inline-block"
                :class="
                  message.from === 'bot'
                    ? 'rounded-bl-none bg-gray-100 text-gray-600'
                    : 'rounded-br-none bg-blue-500 text-white'
                "
                v-html="message.text"
              ></span>
            </div>
          </div>
          <img
            :src="
              message.from === 'bot'
                ? 'https://cdn.icon-icons.com/icons2/1371/PNG/512/robot02_90810.png'
                : 'https://i.pravatar.cc/100?img=7'
            "
            alt=""
            class="w-6 h-6 rounded-full"
            :class="message.from === 'bot' ? 'order-1' : 'order-2'"
          />
        </div>
      </template>
      <div v-if="botTyping" class="flex items-start">
        <div class="flex flex-col space-y-2 text-md leading-tight mx-2 order-2 items-start">
          <div>
            <img
              src="https://support.signal.org/hc/article_attachments/360016877511/typing-animation-3x.gif"
              alt="..."
              class="w-16 ml-6"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="border-t-2 border-gray-200 px-4 pt-4 mb-2 sm:mb-0">
      <div class="relative flex">
        <input
          v-model="prompt"
          @keydown.enter="sendPrompt"
          type="text"
          placeholder="Say something..."
          autocomplete="off"
          autofocus="true"
          class="text-md w-full focus:outline-none focus:placeholder-gray-400 text-gray-600 placeholder-gray-600 pl-5 pr-16 bg-gray-100 border-2 border-gray-200 focus:border-blue-500 rounded-full py-2"
        />
        <div class="absolute right-2 items-center inset-y-0 hidden sm:flex">
          <button
            @click.prevent="sendPrompt"
            class="inline-flex items-center justify-center rounded-full h-8 w-8 transition duration-200 ease-in-out text-white bg-blue-500 hover:bg-blue-600 focus:outline-none"
          >
            <i class="mdi mdi-arrow-right text-xl leading-none"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      prompt: '',
      messages: [],
      botTyping: false
    }
  },
  methods: {
    async sendPrompt() {
      if (!this.prompt.trim()) return

      const userMessage = {
        from: 'user',
        text: this.prompt
      }
      this.messages.push(userMessage)
      this.scrollChat()
      this.botTyping = true

      try {
        const result = await axios.get('http://localhost:8080/gpt/chat', {
          params: { prompt: this.prompt }
        })
        const botMessage = {
          from: 'bot',
          text: result.data
        }
        this.messages.push(botMessage)
      } catch (error) {
        console.error('Error fetching GPT response:', error)
        const errorMessage = {
          from: 'bot',
          text: 'Error fetching response'
        }
        this.messages.push(errorMessage)
      } finally {
        this.botTyping = false
        this.scrollChat()
        this.prompt = ''
      }
    },
    scrollChat() {
      const messagesContainer = document.getElementById('messages')
      messagesContainer.scrollTop = messagesContainer.scrollHeight - messagesContainer.clientHeight
      setTimeout(() => {
        messagesContainer.scrollTop =
          messagesContainer.scrollHeight - messagesContainer.clientHeight
      }, 100)
    }
  }
}
</script>

<style scoped>
/* Tailwind CSS를 사용하여 스타일 적용 */
.text-md {
  font-size: 1rem;
}
</style>

<style>
@import url('https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/materialdesignicons.min.css');
@import url('https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css');
</style>
