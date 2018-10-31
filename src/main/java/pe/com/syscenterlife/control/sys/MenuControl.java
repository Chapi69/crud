/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.syscenterlife.control.sys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pe.com.syscenterlife.modelo.GloPersonas;
import pe.com.syscenterlife.modelo.SysMenu;
import pe.com.syscenterlife.servicio.sys.MenuServicioI;


/**
 *
 * @author LAB_SOFTWARE-DTI
 */
@Controller
public class MenuControl {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    MenuServicioI MenuServicioI;

    
    Logger logger = Logger.getLogger(MenuControl.class.getName());
    
@RequestMapping(value = {"/mainMenu" }, method = RequestMethod.GET)    
public ModelAndView inicio(Locale locale, Map<String,Object> model){
    String welcome=messageSource.getMessage("welcome.message", new Object[]{"David Mamani"}, locale);
    List<SysMenu> lista=MenuServicioI.listarMenu();

    model.put("ListaMenu", lista);
    model.put("message", welcome);
    model.put("startMeeting", "09:10");
    
    return new ModelAndView("sys/menu/mainMenu");
}





@RequestMapping(value = {"/elimMenu" }, method = RequestMethod.GET)
public ModelAndView eliminarMenu(HttpServletRequest r){
    int idMenu=Integer.parseInt(r.getParameter("id"));
    MenuServicioI.eliminarMenu(idMenu);
return new ModelAndView(new RedirectView("/mainMenu"));
}
  
@RequestMapping(value = {"/buscarMenu"}, method = RequestMethod.POST)
public  ModelAndView buscarMenu(Locale locale, Map<String,Object> model, HttpServletRequest r){
    String welcome=messageSource.getMessage("welcome.message", new Object[]{"David Mamani"}, locale);
    String dato=r.getParameter("dato");
    List<SysMenu> lista=MenuServicioI.listarMenuDato(dato);
    model.put("ListaMenu", lista);
    model.put("message", welcome);
    model.put("startMeeting", "09:10");    
return new ModelAndView("sys/menu/mainMenu");
}

@RequestMapping(value = "/formMenu", method = RequestMethod.GET)
public ModelAndView irFormulario(@ModelAttribute("modeloMenu") SysMenu menu,
        BindingResult result, Map<String,Object> model){
    
       model.put("urlAccion","guardarMenu");
    return new ModelAndView("sys/menu/formMenu");
}

@RequestMapping(value = "/guardarMenu", method = RequestMethod.POST)
public ModelAndView guardarMenu(@ModelAttribute("modeloMenu")SysMenu menu,
        BindingResult result, HttpServletRequest r){
        logger.info("Error nombre: "+menu.getNombre());
        try {
        MenuServicioI.guardarMenu(menu);
        return new ModelAndView(new RedirectView("/mainMenu"));
        } catch (Exception e) { 
        logger.info("Error Guardar: "+e.getMessage());    
        return new ModelAndView(new RedirectView("/formMenu"));
        }    
}

@RequestMapping(value = "/formModifMenu1", method = RequestMethod.GET)
public ModelAndView irModificarMenu1(HttpServletRequest r ){
   int id=Integer.parseInt(r.getParameter("id"));
       SysMenu menu=null;
       menu=MenuServicioI.buscarMenuId(id);
       
    return new ModelAndView("sys/menu/formMenu","modeloMenu",menu);
}


@RequestMapping(value = "/formModifMenu", method = RequestMethod.GET)
public ModelAndView irModificarMenu(HttpServletRequest r ){
   int id=Integer.parseInt(r.getParameter("id"));
       SysMenu menu=null;
       menu=MenuServicioI.buscarMenuId(id);
    return new ModelAndView("sys/menu/formMenu","modeloMenu",menu);
}

@RequestMapping(value = "actualizarMenu", method = RequestMethod.POST)
public ModelAndView actualizarMenu(@ModelAttribute("modeloMenu") SysMenu menu,
                                      BindingResult result, HttpServletRequest r ){
        try {
        MenuServicioI.modificarMenu(menu);
        return new ModelAndView(new RedirectView(r.getContextPath()+"/mainMenu"));
    } catch (Exception e) {
        logger.info("Error al modificar: "+e.getMessage());
        return new ModelAndView(new RedirectView(r.getContextPath()+"/formModifMenu?id="+menu.getIdMenu()));
    }
    
}

}
