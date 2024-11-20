<script setup></script>

<template>
  <!-- Footer Section -->
  <footer
    class="bg-gradient-to-r from-gray-800 via-gray-900 to-black text-white py-6 px-8 mt-8 shadow-lg"
  >
    <div
      class="max-w-6xl mx-auto flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0"
    >
      <div class="text-center md:text-left">
        <p class="text-xl font-semibold">&copy; Hwarang Company</p>
        <p class="text-sm text-gray-400">All rights reserved.</p>
      </div>
      <div class="flex space-x-6">
        <a href="#" class="hover:underline text-md">개인정보보호정책</a>
        <a href="#" class="hover:underline text-md">서비스</a>
        <a href="#" class="hover:underline text-md">고객센터</a>
      </div>
      <div class="flex space-x-4">
        <a
          href="https://www.facebook.com"
          target="_blank"
          class="text-gray-400 hover:text-white transition duration-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              d="M22.675 0H1.325C.595 0 0 .595 0 1.325v21.35C0 23.405.595 24 1.325 24h11.495v-9.294H9.692V11.12h3.128V8.414c0-3.1 1.894-4.788 4.66-4.788 1.325 0 2.463.098 2.796.142v3.24h-1.918c-1.504 0-1.796.715-1.796 1.764v2.314h3.59l-.467 3.586h-3.123V24h6.125c.73 0 1.325-.595 1.325-1.325V1.325C24 .595 23.405 0 22.675 0z"
            />
          </svg>
        </a>
        <a
          href="https://www.twitter.com"
          target="_blank"
          class="text-gray-400 hover:text-white transition duration-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              d="M24 4.557a9.93 9.93 0 01-2.828.775 4.932 4.932 0 002.165-2.724 9.865 9.865 0 01-3.127 1.195 4.916 4.916 0 00-8.384 4.482A13.94 13.94 0 011.671 3.15 4.916 4.916 0 003.149 9.724a4.9 4.9 0 01-2.229-.616v.061a4.918 4.918 0 003.946 4.827 4.922 4.922 0 01-2.224.084 4.923 4.923 0 004.604 3.417A9.866 9.866 0 010 21.543a13.896 13.896 0 007.548 2.213c9.057 0 14.01-7.496 14.01-14.01 0-.214-.005-.428-.014-.64A10.025 10.025 0 0024 4.557z"
            />
          </svg>
        </a>
        <a
          href="https://www.linkedin.com"
          target="_blank"
          class="text-gray-400 hover:text-white transition duration-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              d="M22.225 0H1.771C.792 0 0 .771 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.208 24 24 23.227 24 22.271V1.729C24 .771 23.208 0 22.225 0zM7.15 20.451H3.644V9h3.506v11.451zM5.399 7.702c-1.122 0-2.035-.912-2.035-2.035 0-1.122.912-2.035 2.035-2.035 1.122 0 2.035.912 2.035 2.035 0 1.123-.913 2.035-2.035 2.035zM20.451 20.451h-3.506v-5.58c0-1.332-.025-3.042-1.852-3.042-1.852 0-2.135 1.447-2.135 2.944v5.679h-3.506V9h3.366v1.566h.049c.468-.886 1.611-1.82 3.318-1.82 3.545 0 4.195 2.336 4.195 5.372v6.333h-.002z"
            />
          </svg>
        </a>
        <a
          href="https://www.instagram.com"
          target="_blank"
          class="text-gray-400 hover:text-white transition duration-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              d="M12 2.163c3.204 0 3.584.012 4.849.07 1.366.062 2.633.315 3.608 1.29.975.975 1.228 2.242 1.29 3.608.058 1.265.07 1.645.07 4.849s-.012 3.584-.07 4.849c-.062 1.366-.315 2.633-1.29 3.608-.975.975-2.242 1.228-3.608 1.29-1.265.058-1.645.07-4.849.07s-3.584-.012-4.849-.07c-1.366-.062-2.633-.315-3.608-1.29-.975-.975-1.228-2.242-1.29-3.608-.058-1.265-.07-1.645-.07-4.849s.012-3.584.07-4.849c.062-1.366.315-2.633 1.29-3.608.975-.975 2.242-1.228 3.608-1.29 1.265-.058 1.645-.07 4.849-.07M12 0C8.741 0 8.332.012 7.052.07 5.775.127 4.574.4 3.527 1.447 2.48 2.495 2.207 3.696 2.149 4.974 2.092 6.254 2.08 6.663 2.08 10.917s.012 4.663.07 5.943c.058 1.278.33 2.479 1.377 3.527 1.047 1.047 2.249 1.319 3.527 1.377 1.278.058 1.687.07 5.943.07s4.663-.012 5.943-.07c1.278-.058 2.479-.33 3.527-1.377 1.047-1.047 1.319-2.249 1.377-3.527.058-1.278.07-1.687.07-5.943s-.012-4.663-.07-5.943c-.058-1.278-.33-2.479-1.377-3.527-1.047-1.047-2.249-1.319-3.527-1.377C16.663.012 16.254 0 12 0z"
            />
            <path
              d="M12 5.838A6.162 6.162 0 105.838 12 6.158 6.158 0 0012 5.838m0 10.161A4 4 0 1116 12a4 4 0 01-4 4zm6.406-10.845a1.44 1.44 0 11-1.44-1.44 1.44 1.44 0 011.44 1.44z"
            />
          </svg>
        </a>
      </div>
    </div>
  </footer>
</template>

<style scoped></style>
