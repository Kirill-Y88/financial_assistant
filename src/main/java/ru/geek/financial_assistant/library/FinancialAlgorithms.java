package ru.geek.financial_assistant.library;


import ru.geek.financial_assistant.models.IndexDTO;

import java.util.List;

public class FinancialAlgorithms {


    public float getRSI(List<IndexDTO> indexDTOList){
        float priceIncreaseAmount = 0f;
        float priceReductionAmount = 0f;

        for (int i = 1; i < indexDTOList.size(); i++) {

            if( indexDTOList.get(i).getPriceClose() > indexDTOList.get(i-1).getPriceClose()) {
                priceIncreaseAmount = priceIncreaseAmount + indexDTOList.get(i).getPriceClose()-indexDTOList.get(i-1).getPriceClose();
            }
            if( indexDTOList.get(i).getPriceClose() < indexDTOList.get(i-1).getPriceClose()) {
                priceReductionAmount = priceReductionAmount + indexDTOList.get(i-1).getPriceClose()-indexDTOList.get(i).getPriceClose();
            }
        }

      //  System.out.println(" priceIncreaseAmount = " + priceIncreaseAmount);
      //  System.out.println(" priceReductionAmount = " + priceReductionAmount);
    return 100*priceIncreaseAmount/(priceIncreaseAmount+priceReductionAmount);
    }

    /*public float getMACD (List<IndexDTO> indexDTOList){





    }*/

    public float getEMA( float[] indexPriceClose, float k){
        float EMA;

        for (float f : indexPriceClose){
          //  System.out.println("array f ="  + f);
        }

        EMA = k*indexPriceClose[0];
        for (int i = 1; i < indexPriceClose.length; i++) {
            EMA = k*indexPriceClose[i] + (1-k)*EMA;
         //   System.out.println("EMA =" + EMA);
        }
        return EMA;
    }




    





}
