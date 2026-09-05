export default function SelectorMarca({ marcas, valor, alCambiar, error }) {
    return (
    <div className="selector-marca-contenedor">
        <div className="grilla-marcas" role="radiogroup" aria-label="Marca a la que deseas inscribirte">
        {marcas.map((marca) => {
            const seleccionada = valor === marca.id;
            return (
            <button
                key={marca.id}
                type="button"
                role="radio"
                aria-checked={seleccionada}
                className={`opcion-marca ${seleccionada ? "activa" : ""}`}
                onClick={() => alCambiar(marca.id)}
            >
                {marca.nombre}
            </button>
            );
        })}
        </div>
        {error && (
            <p className="campo-error" role="alert">
            {error}
        </p>
        )}
    </div>
    );
}