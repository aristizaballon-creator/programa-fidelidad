const URL_BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

async function solicitar(ruta, opciones = {}) {
    let respuesta;
    try {
    respuesta = await fetch(`${URL_BASE}${ruta}`, {
        headers: { "Content-Type": "application/json" },
        ...opciones,
    });
    } catch (errorRed) {
    const error = new Error(
        "No fue posible conectar con el servidor. Verifica que el backend esté disponible."
    );
    error.causa = errorRed;
    throw error;
    }

    const datos = await respuesta.json().catch(() => null);

    if (!respuesta.ok) {
    const error = new Error(
        datos?.detail || "Ocurrió un error inesperado. Intenta nuevamente."
    );
    error.estado = respuesta.status;
    error.campo = datos?.campo;
    throw error;
    }

    return datos;
}

export const obtenerTiposIdentificacion = () =>
    solicitar("/api/catalogos/tipos-identificacion");

export const obtenerPaises = () => solicitar("/api/catalogos/paises");

export const obtenerDepartamentos = (paisId) =>
    solicitar(`/api/catalogos/departamentos?paisId=${paisId}`);

export const obtenerCiudades = (departamentoId) =>
    solicitar(`/api/catalogos/ciudades?departamentoId=${departamentoId}`);

export const obtenerMarcas = () => solicitar("/api/catalogos/marcas");

export const crearInscripcion = (datos) =>
    solicitar("/api/inscripciones", {
    method: "POST",
    body: JSON.stringify(datos),
    });