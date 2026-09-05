import { useEffect, useState } from "react";
import * as api from "./api.js";
import FormularioInscripcion from "./components/FormularioInscripcion.jsx";
import TarjetaMembresia from "./components/TarjetaMembresia.jsx";
import CarruselMarcas from "./components/CarruselMarcas.jsx";

export default function App() {
  const [marcas, setMarcas] = useState([]);
  const [inscripcionExitosa, setInscripcionExitosa] = useState(null);

  useEffect(() => {
    api.obtenerMarcas().then(setMarcas).catch(() => setMarcas([]));
  }, []);

  function manejarNuevoRegistro() {
    setInscripcionExitosa(null);
  }

  if (inscripcionExitosa) {
    return (
      <div className="pagina">
        <TarjetaMembresia inscripcion={inscripcionExitosa} alVolver={manejarNuevoRegistro} />
      </div>
    );
  }

  return (
    <div className="pagina">
      <div className="seccion-principal">
        <div className="capa-fondo"></div>
        <div className="capa-degradado"></div>
        <img className="sello-hero" src="/images/wax-seal.webp" alt="Sello del club" />

        <div className="contenido-hero">
          <div className="titulo-mixto">
            <span className="escritura">Un club</span>
            <span className="negrita">Exclusivo</span>
          </div>
        </div>

        <div className="contenido-formulario">
          <p className="etiqueta-superior"></p>
          <p className="subtitulo">
          <span className="acento-cursivo">Aquí premiamos tu fidelidad</span>
          </p>
          <p className="subtitulo">
            Registrate para recibir ofertas exclusivas.
          </p>
          <FormularioInscripcion marcas={marcas} alRegistrarConExito={setInscripcionExitosa} />
        </div>
      </div>

      <CarruselMarcas marcas={marcas} />
    </div>
  );
}