package de.rainlessrouting.common.util;

public class Printer {

    public static String prettyPrint(double[][] d)
    {
        StringBuffer sb = new StringBuffer();

        for (int i=0; i < d.length; i++)
        {
        	sb.append("#").append(i).append("/").append(d.length).append(": ");

            double check = 0;

            for (int j=0; j < d[0].length; j++)
            {
                sb.append((int)d[i][j]);
                sb.append(" ");
                check += d[i][j];
            }
            sb.append(" check= ").append(check).append("\n");
            check=0;
        }

        return sb.toString();
    }
}
