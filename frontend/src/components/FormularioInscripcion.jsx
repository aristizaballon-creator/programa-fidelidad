import { useEffect, useState } from "react";
import * as api from "../api.js";
import CampoFormulario from "./CampoFormulario.jsx";
import SelectorMarca from "./SelectorMarca.jsx";

const ESTADO_INICIAL = {
    tipoIdentificacionId: "",
    numeroIdentificacion: "",
    nombres: "",
    apellidos: "",
    fechaNacimiento: "",
    email: "",
    telefono: "",
    direccion: "",
    paisId: "",
    departamentoId: "",
    ciudadId: "",
    ciudadOtra: "",
    marcaId: "",
};

const EXPRESION_NOMBRE = /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s']{2,100}$/;
const EXPRESION_DOCUMENTO = /^[A-Za-z0-9]{5,20}$/;
const EXPRESION_TELEFONO = /^[0-9+()\-\s]{7,20}$/;
const EDAD_MINIMA = 18;

function calcularEdad(fechaTexto) {
    const nacimiento = new Date(fechaTexto);
    const hoy = new Date();
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const noHaCumplidoAnios =
    hoy.getMonth() < nacimiento.getMonth() ||
    (hoy.getMonth() === nacimiento.getMonth() && hoy.getDate() < nacimiento.getDate());
    if (noHaCumplidoAnios) edad -= 1;
    return edad;
}

function validar(formulario, mostrarCiudadOtra) {
    const errores = {};

    if (!formulario.tipoIdentificacionId) {
    errores.tipoIdentificacionId = "Selecciona un tipo de identificación.";
    }

    if (!formulario.numeroIdentificacion.trim()) {
    errores.numeroIdentificacion = "Ingresa tu número de identificación.";
    } else if (!EXPRESION_DOCUMENTO.test(formulario.numeroIdentificacion.trim())) {
    errores.numeroIdentificacion = "Usa solo letras y números, entre 5 y 20 caracteres.";
    }

    if (!formulario.nombres.trim()) {
    errores.nombres = "Ingresa tus nombres.";
    } else if (!EXPRESION_NOMBRE.test(formulario.nombres.trim())) {
    errores.nombres = "Solo se permiten letras y espacios.";
    }

    if (!formulario.apellidos.trim()) {
    errores.apellidos = "Ingresa tus apellidos.";
    } else if (!EXPRESION_NOMBRE.test(formulario.apellidos.trim())) {
    errores.apellidos = "Solo se permiten letras y espacios.";
    }

    if (!formulario.fechaNacimiento) {
    errores.fechaNacimiento = "Ingresa tu fecha de nacimiento.";
    } else {
    const fecha = new Date(formulario.fechaNacimiento);
    if (fecha > new Date()) {
        errores.fechaNacimiento = "La fecha no puede ser futura.";
    } else if (calcularEdad(formulario.fechaNacimiento) < EDAD_MINIMA) {
        errores.fechaNacimiento = `Debes ser mayor de ${EDAD_MINIMA} años para inscribirte.`;
    }
    }

    if (formulario.email.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formulario.email.trim())) {
    errores.email = "Ingresa un correo electrónico válido.";
    }

    if (formulario.telefono.trim() && !EXPRESION_TELEFONO.test(formulario.telefono.trim())) {
    errores.telefono = "Ingresa un teléfono válido.";
    }

    if (!formulario.direccion.trim() || formulario.direccion.trim().length < 5) {
    errores.direccion = "Ingresa una dirección de al menos 5 caracteres.";
    }

    if (!formulario.paisId) errores.paisId = "Selecciona un país.";
    if (!formulario.departamentoId) errores.departamentoId = "Selecciona un departamento.";

    if (mostrarCiudadOtra) {
    if (!formulario.ciudadOtra.trim()) {
        errores.ciudadOtra = "Escribe el nombre de tu ciudad.";
    }
    } else if (!formulario.ciudadId) {
    errores.ciudadId = "Selecciona una ciudad.";
    }

    if (!formulario.marcaId) errores.marcaId = "Selecciona una marca.";

    return errores;
}

export default function FormularioInscripcion({ marcas, alRegistrarConExito }) {    const [tiposIdentificacion, setTiposIdentificacion] = useState([]);
    const [paises, setPaises] = useState([]);
    const [departamentos, setDepartamentos] = useState([]);
    const [ciudades, setCiudades] = useState([]);

    const [formulario, setFormulario] = useState(ESTADO_INICIAL);
    const [errores, setErrores] = useState({});
    const [mostrarCiudadOtra, setMostrarCiudadOtra] = useState(false);

    const [cargandoCatalogos, setCargandoCatalogos] = useState(true);
    const [cargandoDepartamentos, setCargandoDepartamentos] = useState(false);
    const [cargandoCiudades, setCargandoCiudades] = useState(false);
    const [enviando, setEnviando] = useState(false);
    const [errorEnvio, setErrorEnvio] = useState("");
    const [errorCatalogos, setErrorCatalogos] = useState("");

    useEffect(() => {
    Promise.all([
        api.obtenerTiposIdentificacion(),
        api.obtenerPaises(),
    ])
        .then(([tipos, paisesRespuesta]) => {
        setTiposIdentificacion(tipos);
        setPaises(paisesRespuesta);
        })
        .catch(() =>
        setErrorCatalogos(
            "No pudimos cargar el formulario. Verifica que el servidor esté disponible e intenta nuevamente."
        )
        )
        .finally(() => setCargandoCatalogos(false));
    }, []);

    useEffect(() => {
    if (!formulario.paisId) {
        return;
    }
    api
        .obtenerDepartamentos(formulario.paisId)
        .then(setDepartamentos)
        .catch(() => setDepartamentos([]))
        .finally(() => setCargandoDepartamentos(false));
    }, [formulario.paisId]);

    useEffect(() => {
    if (!formulario.departamentoId) {
        return;
    }
    api
        .obtenerCiudades(formulario.departamentoId)
        .then(setCiudades)
        .catch(() => setCiudades([]))
        .finally(() => setCargandoCiudades(false));
    }, [formulario.departamentoId]);

    function actualizarCampo(campo, valor) {
    setFormulario((anterior) => {
        const siguiente = { ...anterior, [campo]: valor };
        if (campo === "paisId") {
        siguiente.departamentoId = "";
        siguiente.ciudadId = "";
        setDepartamentos([]);
        setCiudades([]);
        setCargandoDepartamentos(Boolean(valor));
        setCargandoCiudades(false);
        }
        if (campo === "departamentoId") {
        siguiente.ciudadId = "";
        setCiudades([]);
        setCargandoCiudades(Boolean(valor));
        }
        return siguiente;
    });
    setErrores((anteriores) => ({ ...anteriores, [campo]: undefined }));
    }

    function alternarCiudadOtra() {
    setMostrarCiudadOtra((anterior) => !anterior);
    setFormulario((anterior) => ({ ...anterior, ciudadId: "", ciudadOtra: "" }));
    setErrores((anteriores) => ({ ...anteriores, ciudadId: undefined, ciudadOtra: undefined }));
    }

    async function manejarEnvio(evento) {
    evento.preventDefault();
    setErrorEnvio("");

    const erroresValidacion = validar(formulario, mostrarCiudadOtra);
    setErrores(erroresValidacion);
    if (Object.keys(erroresValidacion).length > 0) return;

    setEnviando(true);
    try {
        const payload = {
        tipoIdentificacionId: Number(formulario.tipoIdentificacionId),
        numeroIdentificacion: formulario.numeroIdentificacion.trim(),
        nombres: formulario.nombres.trim(),
        apellidos: formulario.apellidos.trim(),
        fechaNacimiento: formulario.fechaNacimiento,
        email: formulario.email.trim() || null,
        telefono: formulario.telefono.trim() || null,
        direccion: formulario.direccion.trim(),
        ciudadId: mostrarCiudadOtra ? null : Number(formulario.ciudadId),
        ciudadOtra: mostrarCiudadOtra ? formulario.ciudadOtra.trim() : null,
        marcaId: Number(formulario.marcaId),
        };
        const inscripcion = await api.crearInscripcion(payload);
        const marcaSeleccionada = marcas.find((marca) => marca.id === Number(formulario.marcaId));
        alRegistrarConExito({ ...inscripcion, marca: inscripcion.marca ?? marcaSeleccionada });
    } catch (error) {
        setErrorEnvio(error.message);
    } finally {
        setEnviando(false);
    }
    }

    if (cargandoCatalogos) {
    return <p className="subtitulo">Cargando formulario…</p>;
    }

    if (errorCatalogos) {
    return <div className="aviso aviso-error">{errorCatalogos}</div>;
    }

    return (
    <form className="tarjeta-formulario" onSubmit={manejarEnvio} noValidate>
        <div className="separador">
        <span className="linea"></span>
        <span className="etiqueta">Identificación</span>
        <span className="linea"></span>
        </div>

        <CampoFormulario etiqueta="Tipo de identificación" htmlFor="tipoIdentificacionId" error={errores.tipoIdentificacionId}>
        <select
            id="tipoIdentificacionId"
            value={formulario.tipoIdentificacionId}
            onChange={(evento) => actualizarCampo("tipoIdentificacionId", evento.target.value)}
        >
            <option value="">Selecciona...</option>
            {tiposIdentificacion.map((tipo) => (
            <option key={tipo.id} value={tipo.id}>
                {tipo.nombre}
            </option>
            ))}
        </select>
        </CampoFormulario>

        <CampoFormulario etiqueta="Número de identificación" htmlFor="numeroIdentificacion" error={errores.numeroIdentificacion}>
        <input
            id="numeroIdentificacion"
            type="text"
            value={formulario.numeroIdentificacion}
            onChange={(evento) => actualizarCampo("numeroIdentificacion", evento.target.value)}
            placeholder="1020304050"
        />
        </CampoFormulario>

        <div className="separador">
        <span className="linea"></span>
        <span className="etiqueta">Datos personales</span>
        <span className="linea"></span>
        </div>

        <CampoFormulario etiqueta="Nombres" htmlFor="nombres" error={errores.nombres}>
        <input
            id="nombres"
            type="text"
            value={formulario.nombres}
            onChange={(evento) => actualizarCampo("nombres", evento.target.value)}
        />
        </CampoFormulario>

        <CampoFormulario etiqueta="Apellidos" htmlFor="apellidos" error={errores.apellidos}>
        <input
            id="apellidos"
            type="text"
            value={formulario.apellidos}
            onChange={(evento) => actualizarCampo("apellidos", evento.target.value)}
        />
        </CampoFormulario>

        <CampoFormulario etiqueta="Fecha de nacimiento" htmlFor="fechaNacimiento" error={errores.fechaNacimiento}>
        <input
            id="fechaNacimiento"
            type="date"
            value={formulario.fechaNacimiento}
            onChange={(evento) => actualizarCampo("fechaNacimiento", evento.target.value)}
            max={new Date().toISOString().split("T")[0]}
        />
        </CampoFormulario>

        <CampoFormulario etiqueta="Correo electrónico" htmlFor="email" error={errores.email} opcional>
        <input
            id="email"
            type="email"
            value={formulario.email}
            onChange={(evento) => actualizarCampo("email", evento.target.value)}
            placeholder="tucorreo@ejemplo.com"
        />
        </CampoFormulario>

        <CampoFormulario etiqueta="Teléfono" htmlFor="telefono" error={errores.telefono} opcional>
        <input
            id="telefono"
            type="tel"
            value={formulario.telefono}
            onChange={(evento) => actualizarCampo("telefono", evento.target.value)}
            placeholder="300 123 4567"
        />
        </CampoFormulario>

        <div className="separador">
        <span className="linea"></span>
        <span className="etiqueta">Ubicación</span>
        <span className="linea"></span>
        </div>

        <CampoFormulario etiqueta="País" htmlFor="paisId" error={errores.paisId}>
        <select
            id="paisId"
            value={formulario.paisId}
            onChange={(evento) => actualizarCampo("paisId", evento.target.value)}
        >
            <option value="">Selecciona...</option>
            {paises.map((pais) => (
            <option key={pais.id} value={pais.id}>
                {pais.nombre}
            </option>
            ))}
        </select>
        </CampoFormulario>

        <CampoFormulario
        etiqueta="Departamento"
        htmlFor="departamentoId"
        error={errores.departamentoId}
        ayuda={!formulario.paisId ? "Primero selecciona un país" : undefined}
        >
        <select
            id="departamentoId"
            value={formulario.departamentoId}
            onChange={(evento) => actualizarCampo("departamentoId", evento.target.value)}
            disabled={!formulario.paisId || cargandoDepartamentos}
        >
            <option value="">{cargandoDepartamentos ? "Cargando..." : "Selecciona..."}</option>
            {departamentos.map((departamento) => (
            <option key={departamento.id} value={departamento.id}>
                {departamento.nombre}
            </option>
            ))}
        </select>
        </CampoFormulario>

        {!mostrarCiudadOtra ? (
        <CampoFormulario
            etiqueta="Ciudad"
            htmlFor="ciudadId"
            error={errores.ciudadId}
            ayuda={!formulario.departamentoId ? "Primero selecciona un departamento" : undefined}
        >
            <select
            id="ciudadId"
            value={formulario.ciudadId}
            onChange={(evento) => actualizarCampo("ciudadId", evento.target.value)}
            disabled={!formulario.departamentoId || cargandoCiudades}
            >
            <option value="">{cargandoCiudades ? "Cargando..." : "Selecciona..."}</option>
            {ciudades.map((ciudad) => (
                <option key={ciudad.id} value={ciudad.id}>
                {ciudad.nombre}
                </option>
            ))}
            </select>
            <p className="campo-hint">
            ¿Tu ciudad no aparece? <button type="button" onClick={alternarCiudadOtra}>Escríbela aquí</button>
            </p>
        </CampoFormulario>
        ) : (
        <CampoFormulario etiqueta="Nombre de tu ciudad" htmlFor="ciudadOtra" error={errores.ciudadOtra}>
            <input
            id="ciudadOtra"
            type="text"
            value={formulario.ciudadOtra}
            onChange={(evento) => actualizarCampo("ciudadOtra", evento.target.value)}
            placeholder="Escribe el nombre de tu ciudad"
            />
            <p className="campo-hint">
            <button type="button" onClick={alternarCiudadOtra}>Volver a la lista de ciudades</button>
            </p>
        </CampoFormulario>
        )}

        <CampoFormulario etiqueta="Dirección" htmlFor="direccion" error={errores.direccion}>
        <input
            id="direccion"
            type="text"
            value={formulario.direccion}
            onChange={(evento) => actualizarCampo("direccion", evento.target.value)}
            placeholder="Calle, número, barrio"
        />
        </CampoFormulario>

        <div className="separador">
        <span className="linea"></span>
        <span className="etiqueta">Marca</span>
        <span className="linea"></span>
        </div>

        <SelectorMarca
        marcas={marcas}
        valor={formulario.marcaId ? Number(formulario.marcaId) : null}
        alCambiar={(id) => actualizarCampo("marcaId", id)}
        error={errores.marcaId}
        />

        {errorEnvio && <div className="aviso aviso-error">{errorEnvio}</div>}

        <button type="submit" className="boton-enviar" disabled={enviando}>
        {enviando ? "Enviando..." : "Confirmar inscripción"}
        </button>
    </form>
    );
}