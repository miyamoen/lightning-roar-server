package net.unit8.lightningroar.boundary;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FeedEntryCreateRequest extends BoundaryBase {
    @NotBlank
    private String title;

    @NotNull
    private String summary;

    @NotBlank
    @URL
    private String link;

    @NotNull
    private String updated;
}
