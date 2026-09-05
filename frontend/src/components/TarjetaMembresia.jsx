export default function TarjetaMembresia({ inscripcion, alVolver }) {
const numeroMiembro = String(inscripcion.id).padStart(6, "0");

    return (
    <div className="membresia-contenedor">
        <div
        className="tarjeta-membresia"
        style={{ backgroundImage: "url('/images/membership-bg.webp')" }}
        >
        <div className="membresia-superior">
            <span className="membresia-marca">{inscripcion.marca?.nombre}</span>
            <span className="membresia-numero">
            MIEMBRO
            <br />
            N.° {numeroMiembro}
            </span>
        </div>

        <div className="membresia-sello">
            <img
            src="/images/wax-seal.webp"
            alt="Sello del club"
            style={{ width: "130px", height: "auto" }}
            />
        </div>

        <div>
            <p className="membresia-nombre">
            {inscripcion.nombres} {inscripcion.apellidos}
            </p>
            <p className="membresia-estado">Miembro activo · Club de fidelidad</p>
        </div>

        <p className="membresia-mensaje">
            Ya haces parte del club. Mantente pendiente de nuestros canales para recibir ofertas exclusivas.
        </p>
        </div>

    <button type="button" className="boton-enviar" style={{ maxWidth: "260px", marginTop: "0" }} onClick={alVolver}>        Inscribir a otra persona
        </button>
    </div>
    );
}