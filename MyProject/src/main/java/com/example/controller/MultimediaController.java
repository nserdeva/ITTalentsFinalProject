package com.example.controller;

import com.example.WebInitializer;
import com.example.model.DBManagement.MultimediaDao;
import com.example.model.Multimedia;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * Created by Marina on 29.10.2017 г..
 */
@Controller
public class MultimediaController {
	@Autowired
	MultimediaDao multimediaDao;

	@RequestMapping(value = "/getVideo/{url}", method = RequestMethod.GET)
	public void getUploadedImagesForm(@PathVariable("url") String tempUrl, HttpSession session,
			HttpServletResponse resp) throws IOException {
		if(session.getAttribute("user")==null && session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		try {
			String url = tempUrl + ".mp4";
			File tempImage = new File(
					WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator + url);
			OutputStream out = resp.getOutputStream();
			Path path = tempImage.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getMultimedia/{id}", method = RequestMethod.GET)
	public void getVideo(@PathVariable("id") long id, HttpSession session, HttpServletResponse resp) {
		try {
			if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
				resp.sendRedirect("login");
			}
			Multimedia multimedia = multimediaDao.getMultimediaById(id);
			String url = multimedia.getUrl();
			File tempImage = new File(
					WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator + url);
			OutputStream out = resp.getOutputStream();
			Path path = tempImage.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
	}

}