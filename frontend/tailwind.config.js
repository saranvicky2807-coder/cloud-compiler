/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        dark: {
          900: '#0d1117',
          800: '#161b22',
          750: '#1b222c',
          700: '#21262d',
          600: '#30363d',
          500: '#484f58',
          400: '#8b949e',
          300: '#c9d1d9',
          200: '#e6edf3',
          100: '#f0f6fc'
        },
        brand: {
          50: '#eef2ff',
          100: '#e0e7ff',
          400: '#818cf8',
          500: '#6366f1',
          600: '#4f46e5',
          700: '#4338ca'
        },
        syntax: {
          keyword: '#ff7b72',
          identifier: '#79c0ff',
          number: '#79c0ff',
          string: '#a5d6ff',
          operator: '#ff7b72',
          comment: '#8b949e',
          delimiter: '#c9d1d9'
        }
      },
      fontFamily: {
        mono: ['Fira Code', 'Cascadia Code', 'JetBrains Mono', 'Consolas', 'monospace'],
        sans: ['Inter', 'system-ui', '-apple-system', 'BlinkMacSystemFont', 'sans-serif']
      }
    },
  },
  plugins: [],
}
