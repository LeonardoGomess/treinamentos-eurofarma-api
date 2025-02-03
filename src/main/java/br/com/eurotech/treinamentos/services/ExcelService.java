package br.com.eurotech.treinamentos.services;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eurotech.treinamentos.dto.treinamento.DadosHistoricoTreinamento;
import br.com.eurotech.treinamentos.dto.usuario.DadosAlunoPresenca;
import br.com.eurotech.treinamentos.infra.exception.exceptions_personalizadas.ExcelNaoCriadoException;
import br.com.eurotech.treinamentos.model.Treinamento;
import br.com.eurotech.treinamentos.model.Usuario;
import br.com.eurotech.treinamentos.repository.TreinamentoRepository;
import br.com.eurotech.treinamentos.repository.UsuarioRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import br.com.eurotech.treinamentos.model.DataEHoraFormat;


@Service
public class ExcelService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TreinamentoRepository treinamentoRepository;

    public ByteArrayInputStream createHistoricoFuncionarioExcelFile(Usuario usuario) {
       
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Histórico do Funcionário");
            CellStyle cellStyle = workbook.createCellStyle(); 
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle cellStyleTitle = workbook.createCellStyle();
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);

            
            String[] labels_tb_funcionario = {"Nome","RE","Setor"};
            String[] values_tb_funcionario = {usuario.getNome(),usuario.getRe(),usuario.getSetor().toString()};
            String[] labels_tb_historico_treinamentos = {"Treinamento","Status","Data"};
            List<DadosHistoricoTreinamento> listaDadosHistoricoTreinamento =  treinamentoRepository.findTreinamentosByAluno(usuario.getId()); 

            int numero_row_inicial = 2;
            int numero_row_titulo_tb_funcionario = numero_row_inicial -1; 

            Row row_titulo_tb_funcionario  = sheet.createRow(numero_row_titulo_tb_funcionario);
            Cell cell1 = row_titulo_tb_funcionario.createCell(2);
            cell1.setCellValue("FUNCIONARIO");
            cell1.setCellStyle(cellStyleTitle);
            // Define o intervalo de células a ser mesclado (primeira linha, última linha, primeira coluna, última coluna)
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3)); // Mescla da célula (0,0) até (0,2)
    
            for(int i = 0; i<labels_tb_funcionario.length ;i++){
                Row row = sheet.createRow(numero_row_inicial ++);
                Cell cell = row.createCell(2);
                cell.setCellValue(labels_tb_funcionario[i]);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(values_tb_funcionario[i]);
                cell.setCellStyle(cellStyle);
            }

            numero_row_inicial = 10;
            int numero_row_titulo_tb_treinamento = numero_row_inicial -1;
            Row row_titulo_tb_historico_treinamento  = sheet.createRow(numero_row_titulo_tb_treinamento);
            Cell cell_titulo_2 = row_titulo_tb_historico_treinamento.createCell(2);
            cell_titulo_2.setCellValue("HISTÓRICO DE TREINAMENTOS");
            cell_titulo_2.setCellStyle(cellStyleTitle);
    
            // Define o intervalo de células a ser mesclado (primeira linha, última linha, primeira coluna, última coluna)
            sheet.addMergedRegion(new CellRangeAddress(9, 9, 2, 4));
            Row row_headers_tb_historico_treinamento = sheet.createRow(numero_row_inicial);
            int numero_celula_inicial = 2;
            for(int i = 0; i<labels_tb_historico_treinamentos.length ;i++){
                Cell cell = row_headers_tb_historico_treinamento.createCell(numero_celula_inicial++);
                cell.setCellValue(labels_tb_historico_treinamentos[i]);
                cell.setCellStyle(cellStyle);
            }
            
            for(DadosHistoricoTreinamento dadosHistoricoTreinamento : listaDadosHistoricoTreinamento){
                Row row = sheet.createRow(++numero_row_inicial);
                row.createCell(2).setCellValue(dadosHistoricoTreinamento.nome());
                row.createCell(3).setCellValue(dadosHistoricoTreinamento.aula_concluida() == false ? "AUSENTE" : "PRESENTE");
                row.createCell(4).setCellValue(DataEHoraFormat.returndataFormatada(dadosHistoricoTreinamento.data_inicio()));
            }    
            
            
            // Salve o arquivo em um ByteArrayOutputStream
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
            throw new ExcelNaoCriadoException("Falha ao criar o arquivo excel");
        }
    }

    public ByteArrayInputStream createDadosTreinamentoExcelFile(Long id_treinamento) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Treinamento treinamento  = treinamentoRepository.getReferenceById(id_treinamento);
            Sheet sheet = workbook.createSheet("Histórico de Treinamentos");
            CellStyle cellStyle = workbook.createCellStyle(); 
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle cellStyleTitle = workbook.createCellStyle();
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);

            
            String[] labels_tb_treinamento = {"Nome","Descrição","Data"};
            String[] values_tb_treinamento = {treinamento.getNome(),treinamento.getDescricao(),DataEHoraFormat.returndataFormatada(treinamento.getDataInicio())};
            String[] labels_tb_alunos = {"Nome","RE","Setor","Presente","Assinatura"};
            List<DadosAlunoPresenca> listaAlunosPresenca = usuarioRepository.findDadosAlunoPresencasByTreinamento(id_treinamento); 

            int numero_row_inicial = 2;
            int numero_row_titulo_tb_treinamento = numero_row_inicial -1; 

            Row row_titulo_tb_treinamento  = sheet.createRow(numero_row_titulo_tb_treinamento);
            Cell cell1 = row_titulo_tb_treinamento.createCell(2);
            cell1.setCellValue("TREINAMENTO");
            cell1.setCellStyle(cellStyleTitle);
            // Define o intervalo de células a ser mesclado (primeira linha, última linha, primeira coluna, última coluna)
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3)); // Mescla da célula (0,0) até (0,2)
    
            for(int i = 0; i<labels_tb_treinamento.length ;i++){
                Row row = sheet.createRow(numero_row_inicial ++);
                Cell cell = row.createCell(2);
                cell.setCellValue(labels_tb_treinamento[i]);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(values_tb_treinamento[i]);
                cell.setCellStyle(cellStyle);
            }

            numero_row_inicial = 10;
            int numero_row_titulo_tb_alunos = numero_row_inicial -1;
            Row row_titulo_tb_alunos  = sheet.createRow(numero_row_titulo_tb_alunos);
            Cell cell_titulo_2 = row_titulo_tb_alunos.createCell(2);
            cell_titulo_2.setCellValue("ALUNOS");
            cell_titulo_2.setCellStyle(cellStyleTitle);
    
            // Define o intervalo de células a ser mesclado (primeira linha, última linha, primeira coluna, última coluna)
            sheet.addMergedRegion(new CellRangeAddress(9, 9, 2, 6));
            Row row_headers_tb_historico_treinamento = sheet.createRow(numero_row_inicial);
            int numero_celula_inicial = 2;
            for(int i = 0; i<labels_tb_alunos.length ;i++){
                Cell cell = row_headers_tb_historico_treinamento.createCell(numero_celula_inicial++);
                cell.setCellValue(labels_tb_alunos[i]);
                cell.setCellStyle(cellStyle);
            }
            
            for(DadosAlunoPresenca dadosAlunoPresenca : listaAlunosPresenca){
                Row row = sheet.createRow(++numero_row_inicial);
                row.createCell(2).setCellValue(dadosAlunoPresenca.nome());
                row.createCell(3).setCellValue(dadosAlunoPresenca.re());
                row.createCell(4).setCellValue(dadosAlunoPresenca.setor().toString());
                row.createCell(5).setCellValue(dadosAlunoPresenca.aulaConcluida() == false ? "AUSENTE" : "PRESENTE");
                row.createCell(6).setCellValue(dadosAlunoPresenca.assinatura() != null ? dadosAlunoPresenca.assinatura() : "-");
            }    
            
            
            // Salve o arquivo em um ByteArrayOutputStream
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
            throw new ExcelNaoCriadoException("Falha ao criar o arquivo excel");
        }
    }


}
