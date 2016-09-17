/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const webapp = 'ROOT';
const resources = 'static-resources';
const target = '../../../target';
const gulp = require('gulp');
const ts = require('gulp-typescript');
const tslint = require('gulp-tslint');
const sourcemaps = require('gulp-sourcemaps');
const uglify = require('gulp-uglify');
const concat = require('gulp-concat');
const del = require('del');
const gulpsync = require('gulp-sync')(gulp);
const jade = require('gulp-pug');
const sass = require('gulp-sass');
const es = require('event-stream');
const autoprefixer = require('gulp-autoprefixer');
const KarmaServer = require('karma').Server;
const angularTemplateCache = require('gulp-angular-templatecache');
const gutil = require('gulp-util');
const tscConfig = require('./tsconfig.json');
const tsProject = ts.createProject('./tsconfig.json');
const babel = require('gulp-babel');

/**
 * Run all css & image tasks
 */
gulp.task('css', gulpsync.sync(['images', 'css-third-party']));

/**
 * Copy images from assets to
 */
gulp.task('images', function () {
    return gulp.src('./app/assets/**/*.{gif,jpg,png,svg}')
        .pipe(gulp.dest(target + '/' + resources + '/assets'));
});

gulp.task('css-third-party', function () {
    return gulp.src([
        './node_modules/bootstrap/dist/css/bootstrap.min.css'
    ]).pipe(gulp.dest(target + '/' + resources + '/assets/css/'));
});

gulp.task('js', gulpsync.sync(['compile-ts', 'js-third-party', 'js-bundles']));

gulp.task('lint-ts', function () {
    return gulp.src('./app/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('prose'));
});

gulp.task('compile-ts', function () {
    return tsProject.src()
        .pipe(sourcemaps.init())
        .pipe(ts(tscConfig.compilerOptions))
        .pipe(babel({
            presets: ['es2015']
        }))
        .pipe(uglify({
            mangle: false
        }).on('error',gutil.log))
        .pipe(sourcemaps.write({includeContent: false}))
        .pipe(gulp.dest(target + '/' + resources + '/app/'));
});

gulp.task('js-third-party', function () {

    var scripts = [
        './systemjs.config.js',
        './node_modules/core-js/client/shim.min.js',
        './node_modules/zone.js/dist/zone.js',
        './node_modules/reflect-metadata/Reflect.js',
        './node_modules/systemjs/dist/system.js',
        './node_modules/html5shiv/dist/html5shiv.min.js',
        './node_modules/respond.js/dest/respond.min.js',
        './node_modules/jquery/dist/jquery.min.js',
        './node_modules/jquery-easing/dist/jquery.easing.1.3.umd.min.js',
        './node_modules/bootstrap/dist/js/bootstrap.min.js',
        './node_modules/tether/dist/js/tether.min.js',
        // angular2
        './node_modules/@angular/core/bundles/core.umd.js',
        './node_modules/@angular/common/bundles/common.umd.js',
        './node_modules/@angular/compiler/bundles/compiler.umd.js',
        './node_modules/@angular/platform-browser/bundles/platform-browser.umd.js',
        './node_modules/@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
        './node_modules/@angular/http/bundles/http.umd.js',
        './node_modules/@angular/router/bundles/router.umd.js',
        './node_modules/@angular/forms/bundles/forms.umd.js',
        './node_modules/es6-promise/dist/es6-promise.min.js',
        './node_modules/typescript/lib/typescript.js',
        './node_modules/plugin-typescript/lib/plugin.js'
    ];

    var tasks = [];
    var s;
    for (s in scripts) {
        tasks.push(buildTask(scripts[s]));
    }

    return es.concat(tasks);
});

function buildTask(/*String*/path) {
    console.log("" + path);
    return gulp.src(path).pipe(gulp.dest(target + '/' + resources + '/assets/js/' + path.substr(0, path.lastIndexOf('/')) + '/'));
}

gulp.task('js-bundles', function () {

    var rxjs = gulp.src([
        './node_modules/rxjs/**/*.js'
    ], {base: './node_modules/rxjs/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/rxjs'));

    var wapi = gulp.src([
        './node_modules/angular2-in-memory-web-api/**/*.js'
    ], {base: './node_modules/angular2-in-memory-web-api/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/angular2-in-memory-web-api'));
    return es.concat(rxjs, wapi);
});

gulp.task('pages', function () {
    var app = gulp.src([
        './app/**/*.{jsp,html,htm}'
    ], {base: '.app/'}).pipe(gulp.dest(target + '/' + resources + '/app'));

    var idx = gulp.src([
        './*.{jsp,html,htm}'
    ]).pipe(gulp.dest(target + '/' + resources + '/'));

    return es.concat(app, idx);
});

gulp.task('test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js'
    }, done).start();
});

//TODO - Create gulp tasks for all vendors

//---------TOMEE BEGIN
gulp.task('copy-tomee', function () {
    return gulp.src([
        target + '/' + resources + '/**/*.{html,htm,jsp,jspx,htm,css,js}',
        target + '/' + resources + '/assets'
    ]).pipe(gulp.dest(target + '/apache-tomee/webapps/' + webapp + '/'));
});

gulp.task('clean-tomee', function (callback) {
    return del([
        target + '/' + resources + '/',
        target + '/apache-tomee/webapps/' + webapp + '/app/',
        target + '/apache-tomee/webapps/' + webapp + '/components/'
    ], {
        force: true
    }, callback);
});
//---------TOMEE END

gulp.task('build', gulpsync.sync(['clean-tomee', 'js', 'css', 'pages', 'copy-tomee']));
//gulp.task('build-with-tests', gulpsync.sync(['build', 'test']));

gulp.task('default', gulpsync.sync(['build']), function () {
    gulp.watch(
        ['./app/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build'])
    );
});
