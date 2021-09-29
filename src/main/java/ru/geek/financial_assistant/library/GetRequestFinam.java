package ru.geek.financial_assistant.library;

import com.codeborne.selenide.Selenide;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public  class GetRequestFinam {

    /*
    dtf — формат даты (1 — ггггммдд, 2 — ггммдд, 3 — ддммгг, 4 — дд/мм/гг, 5 — мм/дд/гг)
    tmf — формат времени (1 — ччммсс, 2 — ччмм, 3 — чч: мм: сс, 4 — чч: мм)
    MSOR — выдавать время (0 — начала свечи, 1 — окончания свечи)
    mstimever — выдавать время (НЕ московское — mstimever=0; московское — mstime='on', mstimever='1')
    sep — параметр разделитель полей (1 — запятая (,), 2 — точка (.), 3 — точка с запятой (;), 4 — табуляция (»), 5 — пробел ( ))
    sep2 — параметр разделитель разрядов (1 — нет, 2 — точка (.), 3 — запятая (,), 4 — пробел ( ), 5 — кавычка ('))
    datf — Перечень получаемых данных (#1 — TICKER, PER, DATE, TIME, OPEN, HIGH, LOW, CLOSE, VOL; #2 — TICKER, PER, DATE, TIME, OPEN, HIGH, LOW, CLOSE; #3 — TICKER, PER, DATE, TIME, CLOSE, VOL; #4 — TICKER, PER, DATE, TIME, CLOSE; #5 — DATE, TIME, OPEN, HIGH, LOW, CLOSE, VOL; #6 — DATE, TIME, LAST, VOL, ID, OPER).
    at — добавлять заголовок в файл (0 — нет, 1 — да)
    p - периодичность цен, 5 - 15 мин, 6 - 30 мин, 7 - 1 час, 8 - 1день, 9 - 1неделя
    */

    private int dtf = 1;
    private int tmf = 1;
    private int MSOR = 1;
    private int sep = 1;
    private int sep2 = 1;
    private int datf = 1;
    private int at = 1;
    private int p = 8;

    private int market = 1;
    private String fileFormat = "csv";

    private File download = null;



//  http://export.finam.ru/PFE-RM_210901_210901.csv?market=1&em=2028095&code=PFE-RM&apply=0&df=1&mf=8&yf=2021&from=01.09.2021&dt=1&mt=8&yt=2021&to=01.09.2021&p=7&f=PFE-RM_210901_210901&e=.csv&cn=PFE-RM&dtf=%d&tmf=1&MSOR=1&mstime=on&mstimever=1&sep=1&sep2=1&datf=1&at=1
//  http://export.finam.ru/MOEX.PM-RM:FOBR_210801_210901.csv?market=1&em=2627091&code=MOEX.PM-RM:FOBR&apply=0&df=1&mf=7&yf=2021&from=01.08.2021&dt=1&mt=8&yt=2021&to=01.09.2021&p=7&f=MOEX.PM-RM:FOBR_210801_210901&e=.csv&cn=MOEX.PM-RM:FOBR&dtf=1&tmf=1&MSOR=1&mstime=on&mstimever=1&sep=1&sep2=1&datf=1&at=1
// https://export.finam.ru/export9.out?market=1&em=2676776&token=03AGdBq24GS5K_kgcOA6UwTwJpBoDxQQ_6YWouC_mxcgGCx3eTEaUQGIaZfV4nY6Wd3_M2sEXKZOrFEBOc5f7K6x2Ngyyimybnqv56CRNFMv6BzIiw2WkCpGa7rpN_2ihhEefaVBn8cwMYs0aiCC7lMqgZZYaPJJ5iEqFM44JYeP2QrA390i-ahQTXEgObyIfAcXz591XEw2H0PUPZ_z0q93K1rULvS-mCJYH74HfaNj67NJVQu3ILSX5nIQVXhhhaBx7_vBQyDbU8H-feiL1Aqd16QwxTwsBOiQ4Uw_MAsjDi_Nkoqr74qdWmKNmwd8W3yrMP-J5qsDPNEGfvJxzL4HWsLU7b9ooZJ8LFNyBJsOOwVxFM-4FOlYfOcSR_A8Xt8h-o5Qx0Cw0LGs9gG8dupBX-XWU1fPSEDwfTUCexQ03TfyZ7TYfPlKYA33-bqotPxl7QyLHJitZv&code=MOEX.PAYX-RM:FQBR&apply=0&df=1&mf=0&yf=2020&from=01.01.2020&dt=12&mt=7&yt=2021&to=12.08.2021&p=7&f=MOEX.PAYX-RM:FQBR_200101_210812&e=.txt&cn=MOEX.PAYX-RM:FQBR&dtf=1&tmf=1&MSOR=1&mstime=on&mstimever=1&sep=1&sep2=1&datf=1&at=1
    //страница финама для ручной загрузки
    //https://www.finam.ru/profile/moex-akcii/philip-morris-international_pm-rm/export/?market=1&em=2627091&token=03AGdBq24fsiRbeBUsfJLwDtpqSswnEO2ie0NDCq8ZiOerAEm4NzWbN84LfOeoc-AjWk8gb4itLmVnyKEBKwTQzmdg9x3jXlZ0lR5KZJM_ZWFbZW3cxSFYJ5gLZVi28sssMqTXpR7SoxnrY0af4pbJ8v4Lh-30zpdw4IzQ36-CPQ6t8-LXf-k7YCGTv9Y067mMiBCJSPpSn5hHnZeRRjMEs3PJc5XBzlkln4HX4ztjL3fgJUaDns4qlxkN5KXEoCoQsTTnYqleVUFHHyuoGzH9zj60tN3-PS1shEc6gskaayKvZ1ojJQK0fOWwbsQkn_OLzahviRWz47frlxrVdxJGl4LgiIV00hdpiwuZ0OhPBxCcijsGPOFyx4UdEMLhsAZbhmUAlXi0w87_l2l69_M0XdBx62uJf545l1N5vdKROYuH9eM7zcsM1B4&code=PM-RM&apply=0&df=1&mf=8&yf=2021&from=01.09.2021&dt=1&mt=8&yt=2021&to=01.09.2021&p=7&f=PM-RM_210901_210901&e=.txt&cn=PM-RM&dtf=1&tmf=1&MSOR=1&mstime=on&mstimever=1&sep=1&sep2=1&datf=1&at=1

    //*** int market, long em, String code, - параметры, значения которых нужно вытащить с сайта для различных компаний

    public boolean downloadDataFromSite(long em, String code, LocalDate dateBegin, LocalDate dateEnd) {
       int[] dateBeginA = new int [] { dateBegin.getYear(), dateBegin.getMonth().getValue(), dateBegin.getDayOfMonth() };
       int[] dateEndA = new int[] { dateEnd.getYear(), dateEnd.getMonth().getValue(), dateEnd.getDayOfMonth() } ;



        String s = String.format("http://export.finam.ru/%s_%d%02d%02d_%d%02d%02d.%s?market=%d&em=%d&code=%s&apply=0&df=%d&mf=%d&yf=%d&from=%02d.%02d.%d&dt=%d&mt=%d&yt=%d&from=%02d.%02d.%d&p=%d&f=%s_%d%02d%02d_%d%02d%02d&e=.%s&cn=%s&dtf=1d&tmf=%d&MSOR=%d&mstime=on&mstimever=1&sep=%d&sep2=%d&datf=%d&at=%d",
                code,dateBeginA[0]%100,dateBeginA[1],dateBeginA[2],
                dateEndA [0]%100,dateEndA [1],dateEndA [2], fileFormat, market, em, code,
                dateBeginA [2],dateBeginA [1]-1, dateBeginA [0],
                dateBeginA [2],dateBeginA [1], dateBeginA [0],
                dateEndA [2],dateEndA [1]-1, dateEndA [0],
                dateEndA [2],dateEndA [1], dateEndA [0],
                p, code,dateBeginA [0]%100,dateBeginA [1],dateBeginA [2],
                dateEndA [0]%100,dateEndA [1],dateEndA [2],fileFormat,
                code,dtf,tmf,MSOR,sep,sep2,datf,at);
        try {
           download = Selenide.download(s);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void readDateFromFile(){

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String row;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(download.getPath()));
            while ( (row = bufferedReader.readLine())!=null){
                String[] data = row.split(",");
                for (String s: data    ) {
                    System.out.print(s + " // ");
                }
                System.out.println();
            }
        bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void deleteFile(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Path pathFile = download.toPath();
        Path pathDirectory = pathFile.getParent();
        try {
            Files.delete(pathFile);
            System.out.println("Файл удален");
            Files.delete(pathDirectory);
            System.out.println("Директория удалена");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readDateFromFileTest(){

        try {
            FileReader fileReader = new FileReader("KMAZ_100101_150201.csv");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String row;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("KMAZ_100101_150201.csv"));
            while ( (row = bufferedReader.readLine())!=null){
                String[] data = row.split(",");
                for (String s: data    ) {
                    System.out.print(s + " // ");
                }
                System.out.println();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDownload() {
        return download;
    }
}
