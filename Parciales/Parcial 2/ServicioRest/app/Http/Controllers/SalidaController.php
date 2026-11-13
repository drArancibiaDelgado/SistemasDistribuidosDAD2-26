<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use app\Models\User;

class SalidaController extends Controller
{
    public function store(Request $request){
        $request ->validate([
            'destino'=>'required|string',
            'fecha'=>'required|sting',
            'hora'=>'required|string',
            'precio'=>'requires|int',
            'capacidad'=>'required|int'
        ]);

        $salida=Salida::create($request->all());
        return response()->json ($salida,201);

    }
}
