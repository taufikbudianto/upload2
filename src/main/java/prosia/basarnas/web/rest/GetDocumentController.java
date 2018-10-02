/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.basarnas.repo.AssetTypeImageRepo;
import prosia.basarnas.repo.LogImagesRepo;
import prosia.basarnas.repo.MstKantorSarImagesRepo;
import prosia.basarnas.repo.MstPosSarImagesRepo;
import prosia.basarnas.repo.PerpustakkanRepo;
import prosia.basarnas.repo.PotencyImagesRepo;
import prosia.basarnas.repo.ResAssetImagesRepo;
import prosia.basarnas.repo.ResPersonnelImagesRepo;


/**
 *
 * @author PROSIA
 */
@RestController
public class GetDocumentController {
    @Autowired
    private AssetTypeImageRepo assetTypeImg;
    @Autowired
    private LogImagesRepo logImg;
    @Autowired
    private PotencyImagesRepo potencyImg;
    @Autowired
    private MstPosSarImagesRepo posImg;
    @Autowired
    private MstKantorSarImagesRepo officeImg;
    @Autowired
    private PerpustakkanRepo libraryImg;
    @Autowired
    private ResAssetImagesRepo assetImg;
    @Autowired
    private ResPersonnelImagesRepo personnelImageRepo;
    @RequestMapping(value = "/document/{doc_type}/{img_id}", method = RequestMethod.GET)
    public void showImage(HttpServletResponse response, @PathVariable("doc_type") String doc_type, @PathVariable("img_id") Integer img_id) throws Exception {
//        System.out.println("Data :"+data.findByPotencyid(data2.findByPotencyid(doc_id)).toString());
        System.out.println();
        String imgPath = "C:\\\\Basarnas\\\\";
        System.out.println(doc_type);
        switch (doc_type){
            case "asset":
                imgPath += assetTypeImg.findByImageID(img_id).getPathname().replace("\\", "\\\\");
                break;
            case "log": //
                imgPath += logImg.findByImageID(img_id).getPathname().replace("\\", "\\\\");
                break;
            case "potency":
                imgPath += potencyImg.findByImageID(img_id).getPathname().replace("\\", "\\\\");
                break;
            case "pos":
                imgPath += posImg.findByImageID(img_id).getPathname().replace("\\", "\\\\");
                break;
            case "office":
                imgPath += officeImg.findByImageID(img_id).getPathname().replace("\\", "\\\\");
                break;
            case "library":
                imgPath += libraryImg.findByLibId(img_id).getLibAttachment().replace("\\", "\\\\");
                break;
            case "resasset":
                imgPath += assetImg.findByImageID(img_id).getPathname().replace("\\","\\\\");
                break;
            case "personnel":
                imgPath += personnelImageRepo.findImageByPersonnelIDAndDeleted(String.valueOf(img_id).replace("\\","\\\\"));
                break;
        }
        System.out.println(imgPath);
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            File f = new File(imgPath+"");
            BufferedImage image = null;
            image = ImageIO.read(f);
            ImageIO.write(image, "jpeg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        byte[] imgByte = jpegOutputStream.toByteArray();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
