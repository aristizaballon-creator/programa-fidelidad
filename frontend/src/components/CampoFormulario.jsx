export default function CampoFormulario({ etiqueta, htmlFor, error, opcional = false, ayuda, children }) {
    return (
    <div className="campo">
        <label htmlFor={htmlFor}>
        {etiqueta}
        {opcional && <span className="campo-opcional"> — opcional</span>}
        </label>
        {children}
        {ayuda && !error && <p className="campo-hint">{ayuda}</p>}
        {error && (
        <p className="campo-error" role="alert">
            {error}
        </p>
        )}
    </div>
    );
}