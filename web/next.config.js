// next.config.js
const nextTranslate = require('next-translate');
var path = require("path");

module.exports = nextTranslate({
    // ... rest of the configuration.
    experimental: {
        outputStandalone: true,
    },
    i18n: {
        locales: ["de-DE", "en-US"],
        default: "de-DE",
        localeDetection: true,
    },
    sassOptions: {
        includePaths: [path.join(__dirname, 'styles')]
    },
    images: {
        domains: ['http.cat'],
    },
})
