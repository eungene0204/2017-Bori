package bori.bori.utility;

import bori.bori.user.MyUser;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils
{
    private JsonUtils()
    {
    }

    static public JSONObject writeJSON(MyUser user)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("screenName", user.getScreenName());
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;

    }

}
