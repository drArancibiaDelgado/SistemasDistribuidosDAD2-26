<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Factura extends Model
{
    use HasFactory;

    protected $fillable = [
        'numero_documento',
        'nombre_completo',
        'numero_factura',
        'anio',
        'mes',
        'total_factura',
        'estado',
    ];
}