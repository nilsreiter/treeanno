package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.rpc.AlignDocumentSet.AlignmentThread;

/**
 * Servlet implementation class GetMetaInformation
 */
public class GetMetaInformation extends RPCServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JSONObject json = new JSONObject();

		for (Class<?> simClass : docMan.getSupportedFunctions()) {
			JSONObject clObj = new JSONObject();
			clObj.put("short", simClass.getSimpleName().substring(0, 4));
			clObj.put("canonical", simClass.getCanonicalName());
			clObj.put("readable", simClass.getSimpleName());
			json.append("supportedFunctions", clObj);
		}

		ThreadPoolExecutor service =
				(ThreadPoolExecutor) getServletContext().getAttribute(
						"EXECUTOR");
		JSONObject bgProc = new JSONObject();
		bgProc.put("active", service.getActiveCount());
		bgProc.put("waiting", service.getQueue().size());

		List<AlignmentThread> futures =
				(List<AlignmentThread>) getServletContext().getAttribute(
						"futures");
		for (AlignmentThread future : futures) {
			JSONObject proc = new JSONObject();
			proc.put("id", future.toString());
			proc.put("done", future.getFuture().isDone());
			proc.put("settings", future.getSettings());
			proc.put("start", future.getStart());
			bgProc.append("running", proc);
		}

		json.put("background", bgProc);

		returnJSON(response, json);
	}
}
