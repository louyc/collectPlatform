package com.neusoft.gbw.cp.station.service.transfer.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neusoft.gbw.cp.station.service.transfer.ITransfer;

public class ServletServerHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ITransfer transer;

	public ServletServerHandler(ITransfer transer) {
		this.transer = transer;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			request.getInputStream();
			out.println("Hello GBJG STATION!");
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		BufferedReader reader = null;
		String line = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"GB2312"));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String xml = buffer.toString();
			System.out.println("收到:" + buffer.toString());
			transer.put(xml);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
