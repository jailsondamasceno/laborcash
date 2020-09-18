package luck.materialdesign.tabsnavigator.model;

/**
 * Created by BeS on 14.09.2017.
 */

public class countProjects {
    Long design_count;
    Long sales_count;
    Long it_count;
    Long contru_count;
    Long audiovisual_count;
    Long automoveis_count;
    Long gastronomia_count;
    Long writing_count;

    public countProjects() {
    }

    public countProjects(Long design_count, Long sales_count, Long it_count, Long contru_count, Long audiovisual_count, Long automoveis_count, Long gastronomia_count, Long writing_count) {
        this.design_count = design_count;
        this.sales_count = sales_count;
        this.it_count = it_count;
        this.contru_count = contru_count;
        this.audiovisual_count = audiovisual_count;
        this.automoveis_count = automoveis_count;
        this.gastronomia_count = gastronomia_count;
        this.writing_count = writing_count;
    }

    public Long getDesign_count() {
        return design_count;
    }

    public void setDesign_count(Long design_count) {
        this.design_count = design_count;
    }

    public Long getSales_count() {
        return sales_count;
    }

    public void setSales_count(Long sales_count) {
        this.sales_count = sales_count;
    }

    public Long getIt_count() {
        return it_count;
    }

    public void setIt_count(Long it_count) {
        this.it_count = it_count;
    }

    public Long getContru_count() {
        return contru_count;
    }

    public void setContru_count(Long contru_count) {
        this.contru_count = contru_count;
    }

    public Long getAudiovisual_count() {
        return audiovisual_count;
    }

    public void setAudiovisual_count(Long audiovisual_count) {
        this.audiovisual_count = audiovisual_count;
    }

    public Long getAutomoveis_count() {
        return automoveis_count;
    }

    public void setAutomoveis_count(Long automoveis_count) {
        this.automoveis_count = automoveis_count;
    }

    public Long getGastronomia_count() {
        return gastronomia_count;
    }

    public void setGastronomia_count(Long gastronomia_count) {
        this.gastronomia_count = gastronomia_count;
    }

    public Long getWriting_count() {
        return writing_count;
    }

    public void setWriting_count(Long writing_count) {
        this.writing_count = writing_count;
    }
}
