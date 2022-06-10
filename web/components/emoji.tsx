export default function Emoji({ symbol, label }) {
    return (
        <span
            className="emoji mr-md-1"
            role="img"
            aria-label={label ?? ""}
            aria-hidden={!!label}
        >
            {symbol}
        </span>
    )
}