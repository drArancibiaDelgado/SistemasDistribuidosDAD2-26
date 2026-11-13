using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Runtime.Serialization.Formatters;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;

namespace Central_terminal
{
    /// <summary>
    /// Descripción breve de central
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // Para permitir que se llame a este servicio web desde un script, usando ASP.NET AJAX, quite la marca de comentario de la línea siguiente. 
    // [System.Web.Script.Services.ScriptService]
    public class central : System.Web.Services.WebService
    {

        [WebMethod]
        public String[] VerSalidas( String Destino, String Fecha)
        {
            if (string.IsNullOrEmpty(Destino))
            {
                throw new SoapException("Destino Requerido", SoapException.ClientFaultCode);
            }
            return new string[] { Destino};




        }

        [WebMethod]
        public String[]VenderBoleto(String Ci, int idSalida, int Asiento)
        {

            if (string.IsNullOrEmpty(Ci))
            {
                throw new SoapException("Ci requerido", SoapException.ClientFaultCode);
            }
            return new string[] { Ci};
            


        }

        
    }
}
