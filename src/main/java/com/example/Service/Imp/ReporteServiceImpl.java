package com.example.Service.Imp;

import com.example.Entity.Instrumento;
import com.example.Entity.Pedido;
import com.example.Entity.PedidoDetalle;
import com.example.Repository.InstrumentoRepository;
import com.example.Service.PedidoService;
import com.example.Service.ReporteService;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Override
    public byte[] generarReporteExcel(Date fechaDesde, Date fechaHasta) throws IOException {
        List<Pedido> pedidos = pedidoService.findByFecha(fechaDesde, fechaHasta);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Pedidos");

        // Crear encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Fecha Pedido", "Instrumento", "Marca", "Modelo", "Cantidad", "Precio", "Subtotal"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Llenar datos
        int rowNum = 1;
        for (Pedido pedido : pedidos) {
            for (PedidoDetalle detalle : pedido.getPedidosDetalle()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(pedido.getFechaPedido()));
                row.createCell(1).setCellValue(detalle.getInstrumento().getInstrumento());
                row.createCell(2).setCellValue(detalle.getInstrumento().getMarca());
                row.createCell(3).setCellValue(detalle.getInstrumento().getModelo());
                row.createCell(4).setCellValue(detalle.getCantidad());
                row.createCell(5).setCellValue(detalle.getInstrumento().getPrecio());
                row.createCell(6).setCellValue(detalle.getCantidad() * detalle.getInstrumento().getPrecio());
            }
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    @Override
    public byte[] generateInstrumentoPdf(Long instrumentoId) throws IOException {
        // Busca el instrumento por su ID en la base de datos
        Instrumento instrumento = instrumentoRepository.findById(instrumentoId)
                .orElseThrow(() -> new RuntimeException("Instrumento no encontrado"));

        // Crea un nuevo documento PDF y un ByteArrayOutputStream para almacenarlo
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Añade una página al documento
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Abre un flujo de contenido para escribir en la página
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Agregar imagen del instrumento si está disponible
                String imageDirectory = "src/img/";

                if (instrumento.getImagen() != null && !instrumento.getImagen().isEmpty()) {
                    String imagePath = imageDirectory + instrumento.getImagen(); // Construir la ruta del archivo
                    File imageFile = new File(imagePath);

                    if (imageFile.exists()) { // Verifica si el archivo existe
                        try (InputStream imageStream = new FileInputStream(imageFile)) {
                            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "imagen");
                            contentStream.drawImage(pdImage, 100, 460, 230, 230); // Ajusta la posición y tamaño según sea necesario
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("La imagen " + imagePath + " no existe.");
                    }
                } else {
                    System.out.println("El atributo imagen está vacío o es nulo.");
                }

                // Fondo para el título
                contentStream.setNonStrokingColor(230, 230, 230); // Gris claro
                contentStream.addRect(90, 720, 470, 30);
                contentStream.fill();

                // Escribe el título del documento

                contentStream.setNonStrokingColor(0,0,0);
                addWrappedText(contentStream, instrumento.getInstrumento(), PDType1Font.TIMES_ROMAN, 14, 100, 730, 460);

                // Añadir un fondo gris para los detalles del instrumento
                contentStream.setNonStrokingColor(245, 245, 245); // Otro tono de gris claro
                contentStream.addRect(340, 470, 200, 230);
                contentStream.fill();

                // Borrar cualquier color de fondo establecido anteriormente
                contentStream.setNonStrokingColor(0, 0, 0); // Negro

                // Escribe los detalles del instrumento
                addWrappedText(contentStream, instrumento.getCantidadVendida() + " vendidos", PDType1Font.TIMES_ROMAN, 9, 350, 690, 190);
                addWrappedText(contentStream, "$ " + String.valueOf(instrumento.getPrecio()), PDType1Font.TIMES_ROMAN, 19, 350, 630, 190);
                addWrappedText(contentStream, "Marca: " + instrumento.getMarca(), PDType1Font.TIMES_ROMAN, 11, 350, 600, 190);
                addWrappedText(contentStream, "Modelo: " + instrumento.getModelo(), PDType1Font.TIMES_ROMAN, 11, 350, 580, 190);
                addWrappedText(contentStream, "Costo de Envío:", PDType1Font.TIMES_ROMAN, 9, 350, 560, 190);
                addWrappedText(contentStream, instrumento.getCostoEnvio(), PDType1Font.TIMES_ROMAN, 9, 350, 550, 190);

                addWrappedText(contentStream, "Descripción: ", PDType1Font.TIMES_ROMAN, 12, 100, 440, 460);
                addWrappedText(contentStream, instrumento.getDescripcion(), PDType1Font.TIMES_ROMAN, 12, 100, 420, 460);

                contentStream.close();
            }

            // Guarda el documento en el flujo de bytes
            document.save(baos);
            // Retorna el contenido del PDF como un array de bytes
            return baos.toByteArray();
        }
    }

    // agregar texto en el contenido del PDF
    private void addText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    // agregar texto con ajuste de línea
    private void addWrappedText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float x, float y, float maxWidth) throws IOException {
        contentStream.setFont(font, fontSize);
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (font.getStringWidth(line + word) / 1000 * fontSize > maxWidth) {
                addText(contentStream, line.toString(), font, fontSize, x, y);
                y -= fontSize;
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        addText(contentStream, line.toString(), font, fontSize, x, y);
    }
}



