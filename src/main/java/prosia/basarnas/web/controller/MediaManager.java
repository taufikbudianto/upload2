/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.Data;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author TOMY
 */

@Component
@Scope("session")
@Data
public class MediaManager {
    private byte[] media;
    
    public StreamedContent getStream() throws IOException{
        if(media!=null){
            return new DefaultStreamedContent(new ByteArrayInputStream(media), "image/png");
        }else{
            return new DefaultStreamedContent();
        }
    }
}
