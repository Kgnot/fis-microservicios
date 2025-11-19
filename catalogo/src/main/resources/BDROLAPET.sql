CREATE TABLE "multimedia" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(255),
  "url" text UNIQUE,
  "tamano" bigint,
  "fecha" timestamp,
  "estado" varchar(100)
);

CREATE TABLE "relacionmultimedia" (
  "idImagen" integer,
  "idObjeto" integer,
  "tipoObjeto" varchar(100)
);

CREATE TABLE "usuario" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(200),
  "apellido_1" varchar(200),
  "apellido_2" varchar(200),
  "fecha_de_nacimiento" date,
  "id_documento" integer,
  "email" varchar(100) UNIQUE,
  "strikes" integer,
  "id_Rol" integer
);

CREATE TABLE "publicacion" (
  "id" serial PRIMARY KEY,
  "titulo" varchar(255),
  "fecha" timestamp,
  "id_usuario" integer
);

CREATE TABLE "contenido" (
  "id" serial PRIMARY KEY,
  "likes" integer,
  "fecha" timestamp
);

CREATE TABLE "foro" (
  "id" serial PRIMARY KEY,
  "hilo" text
);

CREATE TABLE "comentario" (
  "id" serial PRIMARY KEY,
  "fecha" timestamp,
  "mensaje" text,
  "tipo_de_objeto" varchar(100),
  "id_objeto" integer
);

CREATE TABLE "punto_de_interes" (
  "id" serial PRIMARY KEY,
  "descripcion" text,
  "nombre" varchar(255),
  "id_direccion" integer
);

CREATE TABLE "direccion" (
  "id" SERIAL PRIMARY KEY,
  "via_principal" VARCHAR(255),
  "numero_via" VARCHAR(50),
  "letra_uno" VARCHAR(10),
  "bis" BOOLEAN,
  "cardinalidad_uno" VARCHAR(10),
  "numero_uno" VARCHAR(50),
  "letra_dos" VARCHAR(10),
  "cardinalidad_dos" VARCHAR(10),
  "numero_dos" VARCHAR(50),
  "complemento" VARCHAR(255)
);

CREATE TABLE "proveedor" (
  "id" serial PRIMARY KEY,
  "id_usuario" integer
);

CREATE TABLE "catalogo" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(255) UNIQUE,
  "descripcion" text,
  "id_proveedor" integer,
  "id_categoria" integer
);

CREATE TABLE "item" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(255),
  "precio" numeric(12,2),
  "fecha" timestamp,
  "valoracion" numeric(3,2),
  "estado" boolean,
  "id_catalogo" integer
);

CREATE TABLE "producto" (
  "id" serial PRIMARY KEY,
  "cantidad" integer,
  "tamano" varchar(100),
  "peso" varchar(100),
  "color" varchar(100),
  "id_item" integer
);

CREATE TABLE "servicio" (
  "id" serial PRIMARY KEY,
  "duracion" interval,
  "horario" time,
  "id_item" integer
);

CREATE TABLE "categoria" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(100)
);

CREATE TABLE "orden_item" (
  "id" serial PRIMARY KEY,
  "id_item" integer,
  "cantidad" integer,
  "valor_unitario" numeric(12,2),
  "subtotal" numeric(12,2),
  "id_orden_compra" integer
);

CREATE TABLE "orden_compra" (
  "id" serial PRIMARY KEY,
  "fecha" timestamp,
  "total" numeric(12,2),
  "id_pago" integer
);

CREATE TABLE "metodo_de_pago" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(20)
);

CREATE TABLE "pago" (
  "id" serial PRIMARY KEY,
  "fecha" timestamp,
  "monto_total" numeric(12,2),
  "id_usuario" integer,
  "id_metodo_pago" integer
);

CREATE TABLE "rol" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(100)
);

CREATE TABLE "operacion" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(200),
  "id_modulo" integer
);

CREATE TABLE "modulo" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(200)
);

CREATE TABLE "rol_operacion" (
  "id" serial PRIMARY KEY,
  "id_rol" integer,
  "id_operacion" integer
);

CREATE TABLE "vehiculo" (
  "id" serial PRIMARY KEY,
  "matricula" varchar(6),
  "tipo_de_vehiculo" text,
  "id_usuario" integer
);

CREATE TABLE "password" (
  "id" serial PRIMARY KEY,
  "fecha" timestamp,
  "chars" text,
  "activo" boolean,
  "id_usuario" integer
);

CREATE TABLE "mensaje" (
  "id" serial PRIMARY KEY,
  "texto" text,
  "id_usuario_emisor" integer,
  "id_usuario_receptor" integer,
  "fecha_envio" timestamp,
  "hora_envio" time
);

CREATE TABLE "mensaje_correo" (
  "id" serial PRIMARY KEY,
  "id_usuario_receptor" integer,
  "recipiente" varchar(255),
  "subject" varchar(500),
  "body" text,
  "fecha" timestamp,
  "estado" varchar(10)
);

CREATE TABLE "documento" (
  "id" serial PRIMARY KEY,
  "tipoDeDocumento" varchar(100),
  "chars" text
);

ALTER TABLE "rol_operacion" ADD FOREIGN KEY ("id_rol") REFERENCES "rol" ("id");

ALTER TABLE "rol_operacion" ADD FOREIGN KEY ("id_operacion") REFERENCES "operacion" ("id");

ALTER TABLE "operacion" ADD FOREIGN KEY ("id_modulo") REFERENCES "modulo" ("id");

ALTER TABLE "usuario" ADD FOREIGN KEY ("id_Rol") REFERENCES "rol" ("id");

ALTER TABLE "password" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id");

ALTER TABLE "vehiculo" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id");

ALTER TABLE "mensaje" ADD FOREIGN KEY ("id_usuario_emisor") REFERENCES "usuario" ("id");

ALTER TABLE "mensaje" ADD FOREIGN KEY ("id_usuario_receptor") REFERENCES "usuario" ("id");

ALTER TABLE "proveedor" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id");

ALTER TABLE "publicacion" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id");

ALTER TABLE "catalogo" ADD FOREIGN KEY ("id_proveedor") REFERENCES "proveedor" ("id");

ALTER TABLE "item" ADD FOREIGN KEY ("id_catalogo") REFERENCES "catalogo" ("id");

ALTER TABLE "producto" ADD FOREIGN KEY ("id_item") REFERENCES "item" ("id");

ALTER TABLE "servicio" ADD FOREIGN KEY ("id_item") REFERENCES "item" ("id");

ALTER TABLE "orden_item" ADD FOREIGN KEY ("id_item") REFERENCES "item" ("id");

ALTER TABLE "orden_item" ADD FOREIGN KEY ("id_orden_compra") REFERENCES "orden_compra" ("id");

ALTER TABLE "orden_compra" ADD FOREIGN KEY ("id_pago") REFERENCES "pago" ("id");

ALTER TABLE "pago" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id");

ALTER TABLE "punto_de_interes" ADD FOREIGN KEY ("id_direccion") REFERENCES "direccion" ("id");

ALTER TABLE "relacionmultimedia" ADD FOREIGN KEY ("idImagen") REFERENCES "multimedia" ("id");

ALTER TABLE "catalogo" ADD FOREIGN KEY ("id_categoria") REFERENCES "categoria" ("id");

ALTER TABLE "pago" ADD FOREIGN KEY ("id_metodo_pago") REFERENCES "metodo_de_pago" ("id");

ALTER TABLE "contenido" ADD FOREIGN KEY ("id") REFERENCES "publicacion" ("id");

ALTER TABLE "contenido" ADD FOREIGN KEY ("id") REFERENCES "foro" ("id");

ALTER TABLE "mensaje_correo" ADD FOREIGN KEY ("id_usuario_receptor") REFERENCES "usuario" ("id");

ALTER TABLE "usuario" ADD FOREIGN KEY ("id_documento") REFERENCES "documento" ("id");
