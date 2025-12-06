DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS tipodocumento CASCADE;
DROP TABLE IF EXISTS documento CASCADE;
DROP TABLE IF EXISTS consentimientomenor CASCADE;
DROP TABLE IF EXISTS contenido CASCADE;
DROP TABLE IF EXISTS publicacion CASCADE;
DROP TABLE IF EXISTS comentario CASCADE;
DROP TABLE IF EXISTS puntointerescomentario CASCADE;
DROP TABLE IF EXISTS forocategoria CASCADE;
DROP TABLE IF EXISTS punto_de_interes CASCADE;
DROP TABLE IF EXISTS catalogo CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS direccion CASCADE;
DROP TABLE IF EXISTS foro CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS mensaje CASCADE;
DROP TABLE IF EXISTS mensaje_correo CASCADE;
DROP TABLE IF EXISTS metodo_de_pago CASCADE;
DROP TABLE IF EXISTS modulo CASCADE;
DROP TABLE IF EXISTS multimedia CASCADE;
DROP TABLE IF EXISTS operacion CASCADE;
DROP TABLE IF EXISTS orden_compra CASCADE;
DROP TABLE IF EXISTS orden_item CASCADE;
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS password CASCADE;
DROP TABLE IF EXISTS unidad_medida CASCADE;
DROP TABLE IF EXISTS color CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS proveedor CASCADE;
DROP TABLE IF EXISTS rol CASCADE;
DROP TABLE IF EXISTS rol_operacion CASCADE;
DROP TABLE IF EXISTS servicio CASCADE;
DROP TABLE IF EXISTS vehiculo CASCADE;



CREATE TABLE usuario (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(200),
  apellido_1 VARCHAR(200),
  apellido_2 VARCHAR(200),
  fecha_de_nacimiento date,
  id_documento integer,
  email VARCHAR(100) UNIQUE NOT NULL,
  strikes integer DEFAULT 0,
  id_Rol integer,
  img_perfil integer
);

CREATE TABLE tipodocumento (
  id_tipodocu SERIAL PRIMARY KEY, 
  nombre VARCHAR(100) NOT NULL
);

CREATE TABLE documento (
  id_documento SERIAL PRIMARY KEY, 
  id_tipodocu integer NOT NULL,
  numero_string VARCHAR(50) NOT NULL,
  fecha_expedicion date NOT NULL
);

CREATE TABLE consentimientomenor (
  id_cons SERIAL PRIMARY KEY, 
  docu_consentimiento integer NOT NULL,
  id_usuario integer NOT NULL
);

CREATE TABLE contenido (
  id SERIAL PRIMARY KEY, 
  likes integer DEFAULT 0,
  fecha_creacion timestamp,
  texto text,
  autor_id integer
);

CREATE TABLE publicacion (
  id SERIAL PRIMARY KEY, 
  titulo VARCHAR(255),
  fecha timestamp,
  id_usuario integer,
  id_img integer, 
  contenido_id integer NOT NULL,
  foro_id integer
);

CREATE TABLE comentario (
  id SERIAL PRIMARY KEY, 
  fecha timestamp,
  mensaje text,
  contenido_id integer NOT NULL,
  comentario_padre_id integer,
  publicacion_id integer NOT NULL
);

CREATE TABLE puntointerescomentario (
  puntocom_id SERIAL PRIMARY KEY, 
  punto_id integer NOT NULL,
  comentario_id integer NOT NULL,
  comentario_date timestamp NOT NULL
);

CREATE TABLE forocategoria (
  forocat_id SERIAL PRIMARY KEY, 
  categoria_id integer NOT NULL,
  foro_id integer NOT NULL
);

CREATE TABLE punto_de_interes (
  id SERIAL PRIMARY KEY, 
  descripcion VARCHAR(50),
  img_pun VARCHAR(500),
  nombre VARCHAR(20),
  id_direccion integer
);

CREATE TABLE catalogo (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(255),
  descripcion text,
  id_proveedor integer,
  id_categoria integer
);

CREATE TABLE categoria (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(100)
);

CREATE TABLE direccion (
  id SERIAL PRIMARY KEY, 
  via_principal VARCHAR(255),
  numero_via VARCHAR(50),
  letra_uno VARCHAR(10),
  bis boolean,
  cardinalidad_uno VARCHAR(10),
  numero_uno VARCHAR(50),
  letra_dos VARCHAR(10),
  cardinalidad_dos VARCHAR(10),
  numero_dos VARCHAR(50),
  complemento VARCHAR(255)
);

CREATE TABLE foro (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(50)
);

CREATE TABLE item (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(255),
  precio numeric(12,2),
  fecha timestamp,
  valoracion numeric(3,2),
  activo boolean,
  id_catalogo integer
);

CREATE TABLE mensaje (
  id SERIAL PRIMARY KEY, 
  texto text,
  id_usuario_emisor integer,
  id_usuario_receptor integer,
  fecha_envio timestamp,
  hora_envio time
);

CREATE TABLE mensaje_correo (
  id SERIAL PRIMARY KEY, 
  id_usuario_receptor integer,
  recipiente VARCHAR(255),
  subject VARCHAR(500),
  body text,
  fecha timestamp,
  estado VARCHAR(10),
  error_message text
);

CREATE TABLE metodo_de_pago (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(20),
  detalles text
);

CREATE TABLE modulo (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(200)
);

CREATE TABLE multimedia (
  id_multi SERIAL PRIMARY KEY, 
  url VARCHAR(500),
  tipo_archivo VARCHAR(100)
);

CREATE TABLE operacion (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(200),
  id_modulo integer
);

CREATE TABLE orden_compra (
  id SERIAL PRIMARY KEY, 
  fecha timestamp,
  monto_total numeric(12,2),
  id_pago integer
);

CREATE TABLE orden_item (
  id SERIAL PRIMARY KEY, 
  id_item integer,
  cantidad integer,
  valor_unitario numeric(12,2),
  subtotal numeric(12,2),
  id_orden_compra integer
);

CREATE TABLE pago (
  id SERIAL PRIMARY KEY, 
  fecha timestamp,
  id_usuario integer,
  id_metodo_pago integer
);

CREATE TABLE password (
  id SERIAL PRIMARY KEY, 
  fecha timestamp,
  chars text,
  activo boolean,
  id_usuario integer
);

CREATE TABLE unidad_medida (
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(20) NOT NULL UNIQUE  -- kg, lb, g, cm, m...
);

CREATE TABLE color (
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE producto (
  id SERIAL PRIMARY KEY,
  cantidad INTEGER NOT NULL,
  tamano VARCHAR(100),
  peso NUMERIC(10,2),
  id_unidad_peso INTEGER REFERENCES unidad_medida(id),
  id_color INTEGER REFERENCES color(id),
  id_item INTEGER
);

CREATE TABLE proveedor (
  id SERIAL PRIMARY KEY, 
  id_usuario integer
);

CREATE TABLE rol (
  id SERIAL PRIMARY KEY, 
  nombre VARCHAR(100)
);

CREATE TABLE rol_operacion (
  id SERIAL PRIMARY KEY, 
  id_rol integer,
  id_operacion integer
);

CREATE TABLE servicio (
  id SERIAL PRIMARY KEY, 
  duracion interval,
  horario time,
  id_item integer
);

CREATE TABLE vehiculo (
  id SERIAL PRIMARY KEY, 
  matricula VARCHAR(6),
  tipo_de_vehiculo text,
  id_usuario integer
);


ALTER TABLE documento ADD CONSTRAINT documento_id_tipodocu_fkey FOREIGN KEY (id_tipodocu) REFERENCES tipodocumento (id_tipodocu);
ALTER TABLE consentimientomenor ADD CONSTRAINT consmenor_docu_consentimiento_fkey FOREIGN KEY (docu_consentimiento) REFERENCES documento (id_documento);
ALTER TABLE consentimientomenor ADD CONSTRAINT consmenor_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE contenido ADD CONSTRAINT contenido_autor_id_fkey FOREIGN KEY (autor_id) REFERENCES usuario (id);
ALTER TABLE publicacion ADD CONSTRAINT publicacion_contenido_id_fkey FOREIGN KEY (contenido_id) REFERENCES contenido (id);
ALTER TABLE publicacion ADD CONSTRAINT publicacion_foro_id_fkey FOREIGN KEY (foro_id) REFERENCES foro (id);
ALTER TABLE comentario ADD CONSTRAINT comentario_contenido_id_fkey FOREIGN KEY (contenido_id) REFERENCES contenido (id);
ALTER TABLE comentario ADD CONSTRAINT comentario_publicacion_id_fkey FOREIGN KEY (publicacion_id) REFERENCES publicacion (id);
ALTER TABLE comentario ADD CONSTRAINT comentario_padre_id_fkey FOREIGN KEY (comentario_padre_id) REFERENCES comentario (id);
ALTER TABLE puntointerescomentario ADD CONSTRAINT puntointerescomentario_punto_id_fkey FOREIGN KEY (punto_id) REFERENCES punto_de_interes (id);
ALTER TABLE puntointerescomentario ADD CONSTRAINT puntointerescomentario_comentario_id_fkey FOREIGN KEY (comentario_id) REFERENCES comentario (id);
ALTER TABLE forocategoria ADD CONSTRAINT forocategoria_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES categoria (id);
ALTER TABLE forocategoria ADD CONSTRAINT forocategoria_foro_id_fkey FOREIGN KEY (foro_id) REFERENCES foro (id);
ALTER TABLE catalogo ADD CONSTRAINT catalogo_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES categoria (id);
ALTER TABLE catalogo ADD CONSTRAINT catalogo_id_proveedor_fkey FOREIGN KEY (id_proveedor) REFERENCES proveedor (id);
ALTER TABLE item ADD CONSTRAINT item_id_catalogo_fkey FOREIGN KEY (id_catalogo) REFERENCES catalogo (id);
ALTER TABLE mensaje ADD CONSTRAINT mensaje_id_usuario_emisor_fkey FOREIGN KEY (id_usuario_emisor) REFERENCES usuario (id);
ALTER TABLE mensaje ADD CONSTRAINT mensaje_id_usuario_receptor_fkey FOREIGN KEY (id_usuario_receptor) REFERENCES usuario (id);
ALTER TABLE mensaje_correo ADD CONSTRAINT mensaje_correo_id_usuario_receptor_fkey FOREIGN KEY (id_usuario_receptor) REFERENCES usuario (id);
ALTER TABLE operacion ADD CONSTRAINT operacion_id_modulo_fkey FOREIGN KEY (id_modulo) REFERENCES modulo (id);
ALTER TABLE orden_compra ADD CONSTRAINT orden_compra_id_pago_fkey FOREIGN KEY (id_pago) REFERENCES pago (id);
ALTER TABLE orden_item ADD CONSTRAINT orden_item_id_item_fkey FOREIGN KEY (id_item) REFERENCES item (id);
ALTER TABLE orden_item ADD CONSTRAINT orden_item_id_orden_compra_fkey FOREIGN KEY (id_orden_compra) REFERENCES orden_compra (id);
ALTER TABLE pago ADD CONSTRAINT pago_id_metodo_pago_fkey FOREIGN KEY (id_metodo_pago) REFERENCES metodo_de_pago (id);
ALTER TABLE pago ADD CONSTRAINT pago_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE password ADD CONSTRAINT password_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE producto ADD CONSTRAINT producto_id_item_fkey FOREIGN KEY (id_item) REFERENCES item (id);
ALTER TABLE proveedor ADD CONSTRAINT proveedor_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE publicacion ADD CONSTRAINT publicacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE punto_de_interes ADD CONSTRAINT punto_de_interes_id_direccion_fkey FOREIGN KEY (id_direccion) REFERENCES direccion (id);
ALTER TABLE rol_operacion ADD CONSTRAINT rol_operacion_id_operacion_fkey FOREIGN KEY (id_operacion) REFERENCES operacion (id);
ALTER TABLE rol_operacion ADD CONSTRAINT rol_operacion_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES rol (id);
ALTER TABLE servicio ADD CONSTRAINT servicio_id_item_fkey FOREIGN KEY (id_item) REFERENCES item (id);
ALTER TABLE usuario ADD CONSTRAINT usuario_id_documento_fkey FOREIGN KEY (id_documento) REFERENCES documento (id_documento);
ALTER TABLE usuario ADD CONSTRAINT usuario_id_rol_fkey FOREIGN KEY (id_Rol) REFERENCES rol (id);

ALTER TABLE usuario ADD CONSTRAINT usuario_img_perfil_fkey FOREIGN KEY (img_perfil) REFERENCES multimedia (id_multi);
ALTER TABLE vehiculo ADD CONSTRAINT vehiculo_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario (id);
ALTER TABLE publicacion ADD CONSTRAINT publicacion_id_img_fkey FOREIGN KEY (id_img) REFERENCES multimedia (id_multi);