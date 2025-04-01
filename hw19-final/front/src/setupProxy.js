const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/auth',
    createProxyMiddleware({
      target: process.env.REACT_APP_DOCKER === 'true'
        ? 'http://auth-service:8081'
        : 'http://localhost:8081',
      changeOrigin: true,
      logLevel: 'debug'
    })
  );

  app.use(
    '/api',
    createProxyMiddleware({
      target: process.env.REACT_APP_DOCKER === 'true'
        ? 'http://backend:8080'
        : 'http://localhost:8080',
      changeOrigin: true,
      logLevel: 'debug'
    })
  );
};