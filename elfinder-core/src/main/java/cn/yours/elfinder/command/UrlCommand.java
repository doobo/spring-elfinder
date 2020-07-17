package cn.yours.elfinder.command;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.service.ElfinderStorage;
import cn.yours.elfinder.service.VolumeHandler;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;

public class UrlCommand extends AbstractJsonCommand implements ElfinderCommand {

    public static final String ENCODING = "utf-8";

    @Override
    protected void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception {
        final String target = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
        VolumeHandler file = findTarget(elfinderStorage, target);
        json.put(ElFinderConstants.ELFINDER_PARAMETER_URL, "http://wwww.5fu8.com");
    }
}
