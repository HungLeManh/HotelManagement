package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.code.hotel_management.service.PDFGeneratorService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class PDFGeneratorServiceImpl implements PDFGeneratorService {
    @Override
    public byte[] generateBookingInvoice(BookingDetailResponse booking) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Booking Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Add booking details
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        document.add(new Paragraph("Booking ID: " + booking.getBookingId(), normalFont));
        document.add(new Paragraph("Customer Name: " + booking.getName(), normalFont));
        document.add(new Paragraph("Check-in Date: " + dateFormat.format(booking.getCheckinDate()), normalFont));
        document.add(new Paragraph("Check-out Date: " + dateFormat.format(booking.getCheckoutDate()), normalFont));
        document.add(new Paragraph("Booking Date: " + dateFormat.format(booking.getBookingDate()), normalFont));
        document.add(Chunk.NEWLINE);

        // Add room details
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        PdfPCell cell1 = new PdfPCell(new Phrase("Room ID"));
        PdfPCell cell2 = new PdfPCell(new Phrase("Price"));

        table.addCell(cell1);
        table.addCell(cell2);

        List<Long> roomIds = booking.getRoomIds();
        List<BigDecimal> prices = booking.getPrices();

        for (int i = 0; i < roomIds.size(); i++) {
            Long roomId = roomIds.get(i);
            BigDecimal price = prices.get(i);
            table.addCell(roomId.toString());
            table.addCell(price.toString());
        }

        document.add(table);

        // Add total
        document.add(new Paragraph("Total Amount: $" + booking.getTotalMoney(), normalFont));

        document.close();
        return out.toByteArray();
    }
}
