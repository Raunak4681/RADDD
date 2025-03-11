import { defineConfig } from 'vite'

export default defineConfig({
  server: {
    port: 5173
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    rollupOptions: {
      input: {
        main: 'index.html'
      }
    }
  },
  base: '/',
  resolve: {
    alias: {
      '@': '/src'
    }
  }
})