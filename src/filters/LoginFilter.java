package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String context_path = ((HttpServletRequest)request).getContextPath();
        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")){ // cssフォルダ内は認証処理から除外
            HttpSession session = ((HttpServletRequest)request).getSession();

            // セッションスコープに保存された従業員（ログインユーザ）情報を取得
            Employee e = (Employee)session.getAttribute("login_employee");

            if(!servlet_path.equals("/login")){ //ログイン画面以外
                // ログアウトしている状態であればログイン画面にリダイレクト
                if(e == null){
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }
                // 従業員管理は管理者のみ閲覧可能
                if(servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0){
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
            }else{ // ログイン画面
                // ログインしているのにログイン画面を表示させようとした場合はシステムのトップページにリダイレクト
                if(e != null){
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
