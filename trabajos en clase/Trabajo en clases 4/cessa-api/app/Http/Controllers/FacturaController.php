<?php

namespace App\Http\Controllers;

use App\Models\Factura;
use Illuminate\Http\Request;

class FacturaController extends Controller
{
    public function index()
    {
        $facturas = Factura::all();

        return response()->json($facturas, 200);
    }

    public function store(Request $request)
    {
        $datos = $request->validate([
            'numero_documento' => 'required|string|max:15',
            'nombre_completo' => 'required|string|max:50',
            'numero_factura' => 'required|integer',
            'anio' => 'required|integer|min:1|max:65535',
            'mes' => 'required|in:Enero,Febrero,Marzo,Abril,Mayo,Junio,Julio,Agosto,Septiembre,Octubre,Noviembre,Diciembre',
            'total_factura' => 'required|numeric|between:0,9999999.99',
            'estado' => 'required|in:emitida,pagada,anulada',
        ]);

        $factura = Factura::create($datos);

        return response()->json($factura, 201);
    }

    public function show(Factura $factura)
    {
        return response()->json($factura, 200);
    }

    public function update(Request $request, Factura $factura)
    {
        $datos = $request->validate([
            'numero_documento' => 'required|string|max:15',
            'nombre_completo' => 'required|string|max:50',
            'numero_factura' => 'required|integer',
            'anio' => 'required|integer|min:1|max:65535',
            'mes' => 'required|in:Enero,Febrero,Marzo,Abril,Mayo,Junio,Julio,Agosto,Septiembre,Octubre,Noviembre,Diciembre',
            'total_factura' => 'required|numeric|between:0,9999999.99',
            'estado' => 'required|in:emitida,pagada,anulada',
        ]);

        $factura->update($datos);

        return response()->json($factura, 200);
    }

    public function destroy(Factura $factura)
    {
        $factura->delete();

        return response()->json(null, 204);
    }
}