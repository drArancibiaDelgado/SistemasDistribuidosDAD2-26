<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class LoginController extends Controller
{
    public function login(Request $request){
        $user =User::where('email',$request->email)->first();
        if(!$user||!Hash::check($request->password,$user->password)){
            return response()->json(['error'=>'Credenciales invalidas'],401);
        }
        $payload=[
            'iat'=>time(),
            'exp'=>time()+3600,
            'data'=>['id'=>user->id]
        ];
        $jwt=JWT::encode($payload,env('JWT_SECRET'),env('JWT_ALGORITHM'));
        return response()->json(['token'=>$jwt,'type'=>'bearer']);
    }
}
