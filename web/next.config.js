// next.config.js
const nextTranslate = require('next-translate');

module.exports = nextTranslate({
    // ... rest of the configuration.
    experimental: {
        outputStandalone: true,
    },
    i18n: {
        locales: ["de-DE", "en-US"],
        default: "de-DE",
        localeDetection: true,
    }
})
