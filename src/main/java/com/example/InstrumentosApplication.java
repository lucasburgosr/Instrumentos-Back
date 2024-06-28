package com.example;

import com.example.Entity.Categoria;
import com.example.Entity.Instrumento;
import com.example.Entity.Usuario;
import com.example.Enum.Rol;
import com.example.Repository.CategoriaRepository;
import com.example.Repository.InstrumentoRepository;
import com.example.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class InstrumentosApplication {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private InstrumentoRepository instrumentoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public static void main(String[] args) {
		SpringApplication.run(InstrumentosApplication.class, args);
	}


	@Bean
	public CommandLineRunner init() {
		return args -> {
			if (usuarioRepository.count() == 0) {
				Usuario admin = new Usuario();
				admin.setNombreUsuario("admin");
				admin.setClave(MD5Encriptador("admin123")); // Usa un método seguro para encriptar
				admin.setRol(Rol.ADMIN);
				usuarioRepository.save(admin);
				System.out.println("Administrador creado");

				Usuario operador = new Usuario();
				operador.setNombreUsuario("operador");
				operador.setClave(MD5Encriptador("op123"));
				operador.setRol(Rol.OPERADOR);
				usuarioRepository.save(operador);
				System.out.println("Operador creado");

				Usuario cliente = new Usuario();
				cliente.setNombreUsuario("cliente");
				cliente.setClave(MD5Encriptador("vis123"));
				cliente.setRol(Rol.CLIENTE);
				usuarioRepository.save(cliente);
				System.out.println("Cliente creado");

			}
			// Crear y guardar una categoría de ejemplo
			Categoria cuerdas = new Categoria();
			cuerdas.setDenominacion("Cuerdas");
			categoriaRepository.save(cuerdas);
			Categoria viento = new Categoria();
			viento.setDenominacion("Viento");
			categoriaRepository.save(viento);
			Categoria percusion = new Categoria();
			percusion.setDenominacion("Percusion");
			categoriaRepository.save(percusion);
			Categoria electronico = new Categoria();
			electronico.setDenominacion("Electronico");
			categoriaRepository.save(electronico);
			Categoria teclado = new Categoria();
			teclado.setDenominacion("Teclado");
			categoriaRepository.save(teclado);

			// Crear y guardar el instrumento hardcodeado
			Instrumento instrumento1 = new Instrumento();
			instrumento1.setInstrumento("Mandolina Instrumento Musical Stagg Sunburst");
			instrumento1.setMarca("Stagg");
			instrumento1.setModelo("M20");
			instrumento1.setImagen("nro10.jpg");
			instrumento1.setCostoEnvio("G");
			instrumento1.setCantidadVendida(28);
			instrumento1.setPrecio(2450);
			instrumento1.setDescripcion("Estas viendo una excelente mandolina de la marca Stagg, con un sonido muy dulce, tapa aros y fondo de tilo, y diapasón de palisandro. Es un instrumento acústico (no se enchufa) de cuerdas dobles (4 pares) con la caja ovalada y cóncava, y el mástil corto. Su utilización abarca variados ámbitos, desde rock, folk, country y ensambles experimentales.");
			instrumento1.setCategoria(cuerdas);
			instrumentoRepository.save(instrumento1);

			Instrumento instrumento2 = new Instrumento();
			instrumento2.setInstrumento("Pandereta Pandero Instrumento Musical");
			instrumento2.setMarca("DyM ventas");
			instrumento2.setModelo("32 sonajas");
			instrumento2.setImagen("nro9.jpg");
			instrumento2.setCostoEnvio("150");
			instrumento2.setCantidadVendida(10);
			instrumento2.setPrecio(325);
			instrumento2.setDescripcion("1 Pandereta - 32 sonajas metálicas. Más de 8 años vendiendo con 100 % de calificaciones POSITIVAS y clientes satisfechos !!");
			instrumento2.setCategoria(percusion);
			instrumentoRepository.save(instrumento2);

			Instrumento instrumento3 = new Instrumento();
			instrumento3.setInstrumento("Triangulo Musical 24 Cm Percusion");
			instrumento3.setMarca("LBP");
			instrumento3.setModelo("24");
			instrumento3.setImagen("nro8.jpg");
			instrumento3.setCostoEnvio("250");
			instrumento3.setCantidadVendida(3);
			instrumento3.setPrecio(260);
			instrumento3.setDescripcion("Triangulo Musical de 24 Centímetros De Acero. ENVIOS POR CORREO O ENCOMIENDA: Se le deberán adicionar $40 en concepto de Despacho y el Costo del envío se abonará al recibir el producto en Terminal, Sucursal OCA o Domicilio");
			instrumento3.setCategoria(percusion);
			instrumentoRepository.save(instrumento3);

			Instrumento instrumento4 = new Instrumento();
			instrumento4.setInstrumento("Bar Chimes Lp Cortina Musical 72 Barras");
			instrumento4.setMarca("FM");
			instrumento4.setModelo("LATIN");
			instrumento4.setImagen("nro7.jpg");
			instrumento4.setCostoEnvio("G");
			instrumento4.setCantidadVendida(2);
			instrumento4.setPrecio(2250);
			instrumento4.setDescripcion("BARCHIME CORTINA MUSICAL DE 25 BARRAS LATIN CUSTOM. Emitimos factura A y B");
			instrumento4.setCategoria(percusion);
			instrumentoRepository.save(instrumento4);

			Instrumento instrumento5 = new Instrumento();
			instrumento5.setInstrumento("Shekeres. Instrumento. Música. Artesanía.");
			instrumento5.setMarca("Azalea Artesanías");
			instrumento5.setModelo("Cuentas de madera");
			instrumento5.setImagen("nro6.jpg");
			instrumento5.setCostoEnvio("300");
			instrumento5.setCantidadVendida(5);
			instrumento5.setPrecio(850);
			instrumento5.setDescripcion("Las calabazas utilizadas para nuestras artesanías son sembradas y cosechadas por nosotros, quienes seleccionamos el mejor fruto para garantizar la calidad del producto y ofrecerle algo creativo y original.");
			instrumento5.setCategoria(percusion);
			instrumentoRepository.save(instrumento5);

			Instrumento instrumento6 = new Instrumento();
			instrumento6.setInstrumento("Antiguo Piano Aleman Con Candelabros.");
			instrumento6.setMarca("Neumeyer");
			instrumento6.setModelo("Stratus");
			instrumento6.setImagen("nro3.jpg");
			instrumento6.setCostoEnvio("2000");
			instrumento6.setCantidadVendida(0);
			instrumento6.setPrecio(17000);
			instrumento6.setDescripcion("Buen dia! Sale a la venta este Piano Alemán Neumeyer con candelabros incluidos. Tiene una talla muy bonita en la madera. Una pieza de calidad.");
			instrumento6.setCategoria(cuerdas);
			instrumentoRepository.save(instrumento6);

			Instrumento instrumento7 = new Instrumento();
			instrumento7.setInstrumento("Guitarra Ukelele Infantil Grande 60cm");
			instrumento7.setMarca("GUITARRA");
			instrumento7.setModelo("UKELELE");
			instrumento7.setImagen("nro4.jpg");
			instrumento7.setCostoEnvio("G");
			instrumento7.setCantidadVendida(5);
			instrumento7.setPrecio(500);
			instrumento7.setDescripcion("Material: Plástico smil madera 4 Cuerdas longitud: 60cm, el mejor regalo para usted, su familia y amigos, adecuado para 3-18 años de edad");
			instrumento7.setCategoria(cuerdas);
			instrumentoRepository.save(instrumento7);

			Instrumento instrumento8 = new Instrumento();
			instrumento8.setInstrumento("Teclado Organo Electronico Musical Instrumento 54 Teclas");
			instrumento8.setMarca("GADNIC");
			instrumento8.setModelo("T01");
			instrumento8.setImagen("nro2.jpg");
			instrumento8.setCostoEnvio("G");
			instrumento8.setCantidadVendida(1375);
			instrumento8.setPrecio(2250);
			instrumento8.setDescripcion("Organo Electrónico GADNIC T01. Display de Led. 54 Teclas. 100 Timbres / 100 Ritmos. 4 1/2 octavas. 8 Percusiones. 8 Canciones de muestra. Grabación y reproducción. Entrada para Micrófono. Salida de Audio (Auriculares / Amplificador). Vibrato. Sustain Incluye Atril Apoya partitura y Micrófono. Dimensiones: 84,5 x 32,5 x 11 cm");
			instrumento8.setCategoria(electronico);
			instrumentoRepository.save(instrumento8);

			Instrumento instrumento9 = new Instrumento();
			instrumento9.setInstrumento("Instrumentos De Percusión Niños Set Musical Con Estuche");
			instrumento9.setMarca("KNIGHT");
			instrumento9.setModelo("LB17");
			instrumento9.setImagen("nro1.jpg");
			instrumento9.setCostoEnvio("300");
			instrumento9.setCantidadVendida(15);
			instrumento9.setPrecio(2700);
			instrumento9.setDescripcion("Estas viendo un excelente y completísimo set de percusion para niños con estuche rígido, equipado con los instrumentos mas divertidos! De gran calidad y sonoridad. Ideal para jardines, escuelas primarias, musicoterapeutas o chicos que se quieran iniciar en la música de la mejor manera. Es un muy buen producto que garantiza entretenimiento en cualquier casa o reunión, ya que esta equipado para que varias personas al mismo tiempo estén tocando un instrumento.");
			instrumento9.setCategoria(percusion);
			instrumentoRepository.save(instrumento9);

			Instrumento instrumento10 = new Instrumento();
			instrumento10.setInstrumento("Batería Musical Infantil Juguete Niño 9 Piezas Palillos");
			instrumento10.setMarca("Bateria");
			instrumento10.setModelo("Infantil");
			instrumento10.setImagen("nro5.jpg");
			instrumento10.setCostoEnvio("250");
			instrumento10.setCantidadVendida(250);
			instrumento10.setPrecio(380);
			instrumento10.setDescripcion("DESCRIPCIÓN: DE 1 A 3 AÑOS. EL SET INCLUYE 5 TAMBORES, PALILLOS Y EL PLATILLO TAL CUAL LAS FOTOS. SONIDOS REALISTAS Y FÁCIL DE MONTAR. MEDIDAS: 40X20X46 CM");
			instrumento10.setCategoria(percusion);
			instrumentoRepository.save(instrumento10);

			Instrumento instrumento11 = new Instrumento();
			instrumento11.setInstrumento("Saxofón Alto");
			instrumento11.setMarca("YAMAHA");
			instrumento11.setModelo("YAS-280");
			instrumento11.setImagen("nro11.jpg");
			instrumento11.setCostoEnvio("G");
			instrumento11.setCantidadVendida(7);
			instrumento11.setPrecio(3200);
			instrumento11.setDescripcion("El saxofón alto Yamaha YAS-280 ofrece una gran calidad de sonido y una excelente durabilidad. Perfecto para estudiantes y músicos avanzados. Incluye estuche y boquilla.");
			instrumento11.setCategoria(viento);
			instrumentoRepository.save(instrumento11);
		};
	}

	private String MD5Encriptador(String clave) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(clave.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
