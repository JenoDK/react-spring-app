const { createProxyMiddleware } = require('http-proxy-middleware');

const SLIMS_AUTH = process.env.REACT_APP_SLIMS_BASE64_AUTH;

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            changeOrigin: true,
        })
    );
    app.use(
        '/rest',
        createProxyMiddleware({
            target: 'http://localhost:8082/SLIMSREST',
            changeOrigin: true,
            headers: {
                'Authorization': 'Basic ' + SLIMS_AUTH
            },
        })
    );
};