const ENLACES_TIENDA = {
    Americanino: "https://www.americanino.com/",
    "American Eagle": "https://www.ae.com.co/",
    Chevignon: "https://www.chevignon.com.co/",
    Esprit: "https://www.esprit.com.co/",
    "Naf Naf": "https://www.nafnaf.com.co/",
    Rifle: "https://www.rifle.com.co/",
};

export default function CarruselMarcas({ marcas }) {
    if (marcas.length === 0) return null;

    const marcasTriplicadas = [...marcas, ...marcas, ...marcas];

    return (
    <div className="carrusel-contenedor">
        <p className="carrusel-etiqueta">Conoce nuestro club de marcas</p>
        <div className="carrusel-pista">
        {marcasTriplicadas.map((marca, indice) => (
            <a
            key={`${marca.id}-${indice}`}
            href={ENLACES_TIENDA[marca.nombre] || "#"}
            className="marca-etiqueta"
            target="_blank"
            rel="noopener noreferrer"
            >
            {marca.nombre}
            </a>
        ))}
        </div>
    </div>
    );
} 