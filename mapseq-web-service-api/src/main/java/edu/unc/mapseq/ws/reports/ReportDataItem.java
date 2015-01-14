package edu.unc.mapseq.ws.reports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportDataItem", propOrder = {})
@XmlRootElement(name = "reportDataItem")
public class ReportDataItem {

    private String name;

    private Double y;

    public ReportDataItem() {
        super();
    }

    public ReportDataItem(String name, Double y) {
        super();
        this.name = name;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
