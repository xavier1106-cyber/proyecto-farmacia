// vite.config.mts
import { URL, fileURLToPath } from "node:url";
import { existsSync } from "node:fs";
import { defineConfig, normalizePath } from "file:///home/xavier/Documentos/infotec-farmacia/gateway/node_modules/vite/dist/node/index.js";
import vue from "file:///home/xavier/Documentos/infotec-farmacia/gateway/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import { viteStaticCopy } from "file:///home/xavier/Documentos/infotec-farmacia/gateway/node_modules/vite-plugin-static-copy/dist/index.js";
var __vite_injected_original_import_meta_url = "file:///home/xavier/Documentos/infotec-farmacia/gateway/vite.config.mts";
var getFileFromRepo = (file) => existsSync(fileURLToPath(new URL(`../node_modules/${file}`, __vite_injected_original_import_meta_url))) ? fileURLToPath(new URL(`../node_modules/${file}`, __vite_injected_original_import_meta_url)) : fileURLToPath(new URL(`./node_modules/${file}`, __vite_injected_original_import_meta_url));
var { getAbsoluteFSPath } = await import("file:///home/xavier/Documentos/infotec-farmacia/gateway/node_modules/swagger-ui-dist/index.js");
var swaggerUiPath = getAbsoluteFSPath();
var config = defineConfig({
  plugins: [
    vue(),
    viteStaticCopy({
      targets: [
        {
          src: [
            `${normalizePath(swaggerUiPath)}/*.{js,css,html,png}`,
            `!${normalizePath(swaggerUiPath)}/**/index.html`,
            normalizePath(getFileFromRepo("axios/dist/axios.min.js")),
            normalizePath(fileURLToPath(new URL("./src/main/webapp/swagger-ui/index.html", __vite_injected_original_import_meta_url)))
          ],
          dest: "swagger-ui"
        }
      ]
    })
  ],
  root: fileURLToPath(new URL("./src/main/webapp/", __vite_injected_original_import_meta_url)),
  publicDir: fileURLToPath(new URL("./target/classes/static/public", __vite_injected_original_import_meta_url)),
  cacheDir: fileURLToPath(new URL("./target/.vite-cache", __vite_injected_original_import_meta_url)),
  build: {
    emptyOutDir: true,
    outDir: fileURLToPath(new URL("./target/classes/static/", __vite_injected_original_import_meta_url)),
    rollupOptions: {
      input: {
        app: fileURLToPath(new URL("./src/main/webapp/index.html", __vite_injected_original_import_meta_url))
      }
    }
  },
  resolve: {
    alias: {
      vue: "@vue/compat/dist/vue.esm-bundler.js",
      "@": fileURLToPath(new URL("./src/main/webapp/app/", __vite_injected_original_import_meta_url))
    }
  },
  define: {
    I18N_HASH: '"generated_hash"',
    SERVER_API_URL: '"/"',
    APP_VERSION: `"${process.env.APP_VERSION ? process.env.APP_VERSION : "DEV"}"`
  },
  server: {
    host: true,
    port: 9e3,
    proxy: Object.fromEntries(
      ["/api", "/management", "/v3/api-docs", "/services"].map((res) => [
        res,
        {
          target: "http://localhost:8080"
        }
      ])
    )
  }
});
var vite_config_default = config;
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcubXRzIl0sCiAgInNvdXJjZXNDb250ZW50IjogWyJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiL2hvbWUveGF2aWVyL0RvY3VtZW50b3MvaW5mb3RlYy1mYXJtYWNpYS9nYXRld2F5XCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCIvaG9tZS94YXZpZXIvRG9jdW1lbnRvcy9pbmZvdGVjLWZhcm1hY2lhL2dhdGV3YXkvdml0ZS5jb25maWcubXRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9ob21lL3hhdmllci9Eb2N1bWVudG9zL2luZm90ZWMtZmFybWFjaWEvZ2F0ZXdheS92aXRlLmNvbmZpZy5tdHNcIjtpbXBvcnQgeyBVUkwsIGZpbGVVUkxUb1BhdGggfSBmcm9tICdub2RlOnVybCc7XG5pbXBvcnQgeyBleGlzdHNTeW5jIH0gZnJvbSAnbm9kZTpmcyc7XG5pbXBvcnQgeyBkZWZpbmVDb25maWcsIG5vcm1hbGl6ZVBhdGggfSBmcm9tICd2aXRlJztcblxuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnO1xuaW1wb3J0IHsgdml0ZVN0YXRpY0NvcHkgfSBmcm9tICd2aXRlLXBsdWdpbi1zdGF0aWMtY29weSc7XG5cbmNvbnN0IGdldEZpbGVGcm9tUmVwbyA9IChmaWxlOiBzdHJpbmcpID0+XG4gIGV4aXN0c1N5bmMoZmlsZVVSTFRvUGF0aChuZXcgVVJMKGAuLi9ub2RlX21vZHVsZXMvJHtmaWxlfWAsIGltcG9ydC5tZXRhLnVybCkpKVxuICAgID8gZmlsZVVSTFRvUGF0aChuZXcgVVJMKGAuLi9ub2RlX21vZHVsZXMvJHtmaWxlfWAsIGltcG9ydC5tZXRhLnVybCkpXG4gICAgOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoYC4vbm9kZV9tb2R1bGVzLyR7ZmlsZX1gLCBpbXBvcnQubWV0YS51cmwpKTtcblxuY29uc3QgeyBnZXRBYnNvbHV0ZUZTUGF0aCB9ID0gYXdhaXQgaW1wb3J0KCdzd2FnZ2VyLXVpLWRpc3QnKTtcbmNvbnN0IHN3YWdnZXJVaVBhdGggPSBnZXRBYnNvbHV0ZUZTUGF0aCgpO1xuXG4vLyBlc2xpbnQtZGlzYWJsZS1uZXh0LWxpbmUgcHJlZmVyLWNvbnN0XG5jb25zdCBjb25maWcgPSBkZWZpbmVDb25maWcoe1xuICBwbHVnaW5zOiBbXG4gICAgdnVlKCksXG4gICAgdml0ZVN0YXRpY0NvcHkoe1xuICAgICAgdGFyZ2V0czogW1xuICAgICAgICB7XG4gICAgICAgICAgc3JjOiBbXG4gICAgICAgICAgICBgJHtub3JtYWxpemVQYXRoKHN3YWdnZXJVaVBhdGgpfS8qLntqcyxjc3MsaHRtbCxwbmd9YCxcbiAgICAgICAgICAgIGAhJHtub3JtYWxpemVQYXRoKHN3YWdnZXJVaVBhdGgpfS8qKi9pbmRleC5odG1sYCxcbiAgICAgICAgICAgIG5vcm1hbGl6ZVBhdGgoZ2V0RmlsZUZyb21SZXBvKCdheGlvcy9kaXN0L2F4aW9zLm1pbi5qcycpKSxcbiAgICAgICAgICAgIG5vcm1hbGl6ZVBhdGgoZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYy9tYWluL3dlYmFwcC9zd2FnZ2VyLXVpL2luZGV4Lmh0bWwnLCBpbXBvcnQubWV0YS51cmwpKSksXG4gICAgICAgICAgXSxcbiAgICAgICAgICBkZXN0OiAnc3dhZ2dlci11aScsXG4gICAgICAgIH0sXG4gICAgICBdLFxuICAgIH0pLFxuICBdLFxuICByb290OiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vc3JjL21haW4vd2ViYXBwLycsIGltcG9ydC5tZXRhLnVybCkpLFxuICBwdWJsaWNEaXI6IGZpbGVVUkxUb1BhdGgobmV3IFVSTCgnLi90YXJnZXQvY2xhc3Nlcy9zdGF0aWMvcHVibGljJywgaW1wb3J0Lm1ldGEudXJsKSksXG4gIGNhY2hlRGlyOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vdGFyZ2V0Ly52aXRlLWNhY2hlJywgaW1wb3J0Lm1ldGEudXJsKSksXG4gIGJ1aWxkOiB7XG4gICAgZW1wdHlPdXREaXI6IHRydWUsXG4gICAgb3V0RGlyOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vdGFyZ2V0L2NsYXNzZXMvc3RhdGljLycsIGltcG9ydC5tZXRhLnVybCkpLFxuICAgIHJvbGx1cE9wdGlvbnM6IHtcbiAgICAgIGlucHV0OiB7XG4gICAgICAgIGFwcDogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYy9tYWluL3dlYmFwcC9pbmRleC5odG1sJywgaW1wb3J0Lm1ldGEudXJsKSksXG4gICAgICB9LFxuICAgIH0sXG4gIH0sXG4gIHJlc29sdmU6IHtcbiAgICBhbGlhczoge1xuICAgICAgdnVlOiAnQHZ1ZS9jb21wYXQvZGlzdC92dWUuZXNtLWJ1bmRsZXIuanMnLFxuICAgICAgJ0AnOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vc3JjL21haW4vd2ViYXBwL2FwcC8nLCBpbXBvcnQubWV0YS51cmwpKSxcbiAgICB9LFxuICB9LFxuICBkZWZpbmU6IHtcbiAgICBJMThOX0hBU0g6ICdcImdlbmVyYXRlZF9oYXNoXCInLFxuICAgIFNFUlZFUl9BUElfVVJMOiAnXCIvXCInLFxuICAgIEFQUF9WRVJTSU9OOiBgXCIke3Byb2Nlc3MuZW52LkFQUF9WRVJTSU9OID8gcHJvY2Vzcy5lbnYuQVBQX1ZFUlNJT04gOiAnREVWJ31cImAsXG4gIH0sXG4gIHNlcnZlcjoge1xuICAgIGhvc3Q6IHRydWUsXG4gICAgcG9ydDogOTAwMCxcbiAgICBwcm94eTogT2JqZWN0LmZyb21FbnRyaWVzKFxuICAgICAgWycvYXBpJywgJy9tYW5hZ2VtZW50JywgJy92My9hcGktZG9jcycsICcvc2VydmljZXMnXS5tYXAocmVzID0+IFtcbiAgICAgICAgcmVzLFxuICAgICAgICB7XG4gICAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDgwJyxcbiAgICAgICAgfSxcbiAgICAgIF0pLFxuICAgICksXG4gIH0sXG59KTtcblxuLy8gamhpcHN0ZXItbmVlZGxlLWFkZC12aXRlLWNvbmZpZyAtIEpIaXBzdGVyIHdpbGwgYWRkIGN1c3RvbSBjb25maWdcblxuZXhwb3J0IGRlZmF1bHQgY29uZmlnO1xuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUFvVSxTQUFTLEtBQUsscUJBQXFCO0FBQ3ZXLFNBQVMsa0JBQWtCO0FBQzNCLFNBQVMsY0FBYyxxQkFBcUI7QUFFNUMsT0FBTyxTQUFTO0FBQ2hCLFNBQVMsc0JBQXNCO0FBTDBLLElBQU0sMkNBQTJDO0FBTzFQLElBQU0sa0JBQWtCLENBQUMsU0FDdkIsV0FBVyxjQUFjLElBQUksSUFBSSxtQkFBbUIsSUFBSSxJQUFJLHdDQUFlLENBQUMsQ0FBQyxJQUN6RSxjQUFjLElBQUksSUFBSSxtQkFBbUIsSUFBSSxJQUFJLHdDQUFlLENBQUMsSUFDakUsY0FBYyxJQUFJLElBQUksa0JBQWtCLElBQUksSUFBSSx3Q0FBZSxDQUFDO0FBRXRFLElBQU0sRUFBRSxrQkFBa0IsSUFBSSxNQUFNLE9BQU8sK0ZBQWlCO0FBQzVELElBQU0sZ0JBQWdCLGtCQUFrQjtBQUd4QyxJQUFNLFNBQVMsYUFBYTtBQUFBLEVBQzFCLFNBQVM7QUFBQSxJQUNQLElBQUk7QUFBQSxJQUNKLGVBQWU7QUFBQSxNQUNiLFNBQVM7QUFBQSxRQUNQO0FBQUEsVUFDRSxLQUFLO0FBQUEsWUFDSCxHQUFHLGNBQWMsYUFBYSxDQUFDO0FBQUEsWUFDL0IsSUFBSSxjQUFjLGFBQWEsQ0FBQztBQUFBLFlBQ2hDLGNBQWMsZ0JBQWdCLHlCQUF5QixDQUFDO0FBQUEsWUFDeEQsY0FBYyxjQUFjLElBQUksSUFBSSwyQ0FBMkMsd0NBQWUsQ0FBQyxDQUFDO0FBQUEsVUFDbEc7QUFBQSxVQUNBLE1BQU07QUFBQSxRQUNSO0FBQUEsTUFDRjtBQUFBLElBQ0YsQ0FBQztBQUFBLEVBQ0g7QUFBQSxFQUNBLE1BQU0sY0FBYyxJQUFJLElBQUksc0JBQXNCLHdDQUFlLENBQUM7QUFBQSxFQUNsRSxXQUFXLGNBQWMsSUFBSSxJQUFJLGtDQUFrQyx3Q0FBZSxDQUFDO0FBQUEsRUFDbkYsVUFBVSxjQUFjLElBQUksSUFBSSx3QkFBd0Isd0NBQWUsQ0FBQztBQUFBLEVBQ3hFLE9BQU87QUFBQSxJQUNMLGFBQWE7QUFBQSxJQUNiLFFBQVEsY0FBYyxJQUFJLElBQUksNEJBQTRCLHdDQUFlLENBQUM7QUFBQSxJQUMxRSxlQUFlO0FBQUEsTUFDYixPQUFPO0FBQUEsUUFDTCxLQUFLLGNBQWMsSUFBSSxJQUFJLGdDQUFnQyx3Q0FBZSxDQUFDO0FBQUEsTUFDN0U7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsU0FBUztBQUFBLElBQ1AsT0FBTztBQUFBLE1BQ0wsS0FBSztBQUFBLE1BQ0wsS0FBSyxjQUFjLElBQUksSUFBSSwwQkFBMEIsd0NBQWUsQ0FBQztBQUFBLElBQ3ZFO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sV0FBVztBQUFBLElBQ1gsZ0JBQWdCO0FBQUEsSUFDaEIsYUFBYSxJQUFJLFFBQVEsSUFBSSxjQUFjLFFBQVEsSUFBSSxjQUFjLEtBQUs7QUFBQSxFQUM1RTtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sT0FBTyxPQUFPO0FBQUEsTUFDWixDQUFDLFFBQVEsZUFBZSxnQkFBZ0IsV0FBVyxFQUFFLElBQUksU0FBTztBQUFBLFFBQzlEO0FBQUEsUUFDQTtBQUFBLFVBQ0UsUUFBUTtBQUFBLFFBQ1Y7QUFBQSxNQUNGLENBQUM7QUFBQSxJQUNIO0FBQUEsRUFDRjtBQUNGLENBQUM7QUFJRCxJQUFPLHNCQUFROyIsCiAgIm5hbWVzIjogW10KfQo=
