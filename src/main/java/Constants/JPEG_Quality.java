package Constants;

public enum JPEG_Quality {
    Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, MAX, MIN, DEFAULT, JPEGStandard;
  
    public final int[][] getLuminanceTable() {
      switch (this) {
      case Q0:
        return QuantizationTables.Luminance00;
      case Q1:
        return QuantizationTables.Luminance01;
      case Q2:
        return QuantizationTables.Luminance02;
      case Q3:
        return QuantizationTables.Luminance03;
      case Q4:
        return QuantizationTables.Luminance04;
      case Q5:
        return QuantizationTables.Luminance05;
      case Q6:
        return QuantizationTables.Luminance06;
      case Q7:
        return QuantizationTables.Luminance07;
      case Q8:
        return QuantizationTables.Luminance08;
      case Q9:
        return QuantizationTables.Luminance09;
      case Q10:
        return QuantizationTables.Luminance10;
      case Q11:
        return QuantizationTables.Luminance11;
      case Q12:
        return QuantizationTables.Luminance12;
      case MAX:
        return QuantizationTables.Luminance12;
      case MIN:
        return QuantizationTables.Luminance00;
      case DEFAULT:
        return QuantizationTables.Luminance09;
      case JPEGStandard:
        return QuantizationTables.JPEGLuminanceStandard;
      default:
        return QuantizationTables.Luminance07;
      }
    }
  
    public final int[][] getChrominanceTable() {
      switch (this) {
      case Q0:
        return QuantizationTables.Chrominance00;
      case Q1:
        return QuantizationTables.Chrominance01;
      case Q2:
        return QuantizationTables.Chrominance02;
      case Q3:
        return QuantizationTables.Chrominance03;
      case Q4:
        return QuantizationTables.Chrominance04;
      case Q5:
        return QuantizationTables.Chrominance05;
      case Q6:
        return QuantizationTables.Chrominance06;
      case Q7:
        return QuantizationTables.Chrominance07;
      case Q8:
        return QuantizationTables.Chrominance08;
      case Q9:
        return QuantizationTables.Chrominance09;
      case Q10:
        return QuantizationTables.Chrominance10;
      case Q11:
        return QuantizationTables.Chrominance11;
      case Q12:
        return QuantizationTables.Chrominance12;
      case MAX:
        return QuantizationTables.Chrominance12;
      case MIN:
        return QuantizationTables.Chrominance00;
      case DEFAULT:
        return QuantizationTables.Chrominance06;
      case JPEGStandard:
        return QuantizationTables.JPEGChrominanceStandard;
      default:
        return QuantizationTables.Chrominance07;
      }
    }
  }